package com.nhnacademy.book.book.service;

import com.nhnacademy.book.book.dto.response.aladin.AladinBookListResponse;
import com.nhnacademy.book.book.dto.response.aladin.AladinResponse;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AladinApiService {

    @Value("${aladin.api.url}")
    private String aladinApiUrl;

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final SellingBookRepository sellingBookRepository;


    public AladinApiService(RestTemplate restTemplate, BookRepository bookRepository,
                            PublisherRepository publisherRepository, CategoryRepository categoryRepository,
                            AuthorRepository authorRepository, SellingBookRepository sellingBookRepository) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.sellingBookRepository = sellingBookRepository;
    }

    @Transactional
    public void saveBooksFromAladinApi() {
        try {
            AladinBookListResponse response = restTemplate.getForObject(aladinApiUrl, AladinBookListResponse.class);

            if (response != null && response.getBooks() != null) {
                response.getBooks().forEach(bookData -> {
                    if (!bookRepository.existsByBookIsbn13(bookData.getIsbn13())) {
                        // Book 저장
                        Book book = mapToBookEntity(bookData);
                        bookRepository.save(book);

                        // SellingBook 저장
                        SellingBook sellingBook = createSellingBook(book, bookData);
                        sellingBookRepository.save(sellingBook);
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("알라딘 API 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("알라딘 API 호출 실패", e);
        }
    }



    private Book mapToBookEntity(AladinResponse response) {
        Publisher publisher = findOrCreatePublisher(response.getPublisher());
        Book book = new Book();
        book.setPublisher(publisher);
        book.setBookTitle(response.getTitle());
        book.setBookIndex(response.getDescription()); // 목차는 API에 없으므로 null 그대로 저장
        book.setBookDescription(response.getDescription()); // 설명도 null 그대로 저장
        book.setBookIsbn13(response.getIsbn13());
        book.setBookPriceStandard(BigDecimal.valueOf(response.getPriceStandard()));
        book.setBookPubDate(LocalDate.parse(response.getPubDate(), DateTimeFormatter.ISO_DATE));

        if (response.getAuthor() != null) {
            for (String authorName : response.getAuthor().split(",")) {
                Author author = findOrCreateAuthor(authorName.trim());
                book.addAuthor(author);
            }
        }

        return book;
    }



    private Publisher findOrCreatePublisher(String publisherName) {
        return publisherRepository.findByPublisherName(publisherName)
                .orElseGet(() -> {
                    Publisher publisher = new Publisher();
                    publisher.setPublisherName(publisherName);
                    return publisherRepository.save(publisher);
                });
    }

    private void connectCategoriesToBook(Book book, String categoryName) {
        if (categoryName != null && !categoryName.isBlank()) {
            Category category = findOrCreateCategory(categoryName);

            // Book 저장 후 BookCategory 연결
            if (book.getBookId() == null) {
                book = bookRepository.save(book); // Book 저장
            }

            BookCategory bookCategory = new BookCategory(book, category);
            book.getBookCategories().add(bookCategory);
            category.getBookCategories().add(bookCategory);
        } else {
            System.out.println("카테고리 이름이 비어있어 연결하지 않음.");
        }
    }


    private Category findOrCreateCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setCategoryName(categoryName);
                    category.setCategoryDepth(1);
                    return categoryRepository.save(category);
                });
    }

    private void connectAuthorsToBook(Book book, String authorNames) {
        if (authorNames != null) {
            String[] authors = authorNames.split(",");
            for (String authorName : authors) {
                Author author = findOrCreateAuthor(authorName.trim());
                book.addAuthor(author); // 양방향 관계 설정
            }
        }
    }

    private Author findOrCreateAuthor(String authorName) {
        return authorRepository.findByAuthorName(authorName)
                .orElseGet(() -> {
                    Author author = new Author();
                    author.setAuthorName(authorName);
                    return authorRepository.save(author);
                });
    }

    private SellingBook createSellingBook(Book book, AladinResponse bookData) {
        SellingBook sellingBook = new SellingBook();
        sellingBook.setBook(book);
        sellingBook.setSellingBookPrice(BigDecimal.valueOf(bookData.getPriceStandard()));
        sellingBook.setSellingBookPackageable(false);
        sellingBook.setSellingBookStock(100);
        sellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        sellingBook.setUsed(false);
        sellingBook.setSellingBookViewCount(0L);
        return sellingBook;
    }


}
