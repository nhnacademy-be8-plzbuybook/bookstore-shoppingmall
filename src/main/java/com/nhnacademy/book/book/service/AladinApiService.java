package com.nhnacademy.book.book.service;


import com.nhnacademy.book.book.dto.response.AladinBookListResponse;
import com.nhnacademy.book.book.dto.response.AladinResponse;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AladinApiService {

    @Value("${aladin.api.url}")
    private String aladinApiUrl;

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    public AladinApiService(RestTemplate restTemplate, BookRepository bookRepository,
                            PublisherRepository publisherRepository, CategoryRepository categoryRepository) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void saveBooksFromAladinApi() {
        try {
            AladinBookListResponse response = restTemplate.getForObject(aladinApiUrl, AladinBookListResponse.class);

            if (response != null && response.getBooks() != null) {
                response.getBooks().forEach(bookData -> {
                    if (!bookRepository.existsByBookIsbnOrBookIsbn13(bookData.getBookIsbn(), bookData.getBookIsbn13())) {
                        Book book = mapToBookEntity(bookData);
                        connectCategoriesToBook(book, bookData.getCategory());
                        bookRepository.save(book);
                    } else {
                        System.out.println("중복된 ISBN: " + bookData.getBookIsbn());
                    }
                });
            } else {
                throw new IllegalStateException("알라딘 API에서 데이터를 가져올 수 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("알라딘 API 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("알라딘 API 호출 실패", e);
        }
    }

    private Book mapToBookEntity(AladinResponse response) {
        Publisher publisher = findOrCreatePublisher(response.getPublisher().getPublisherName());
        Book book = new Book();
        book.setPublisher(publisher);
        book.setBookTitle(response.getBookTitle());
        book.setBookIndex(response.getBookIndex());
        book.setBookDescription(response.getBookDescription());
        book.setBookIsbn(response.getBookIsbn());
        book.setBookIsbn13(response.getBookIsbn13());
        book.setBookPriceStandard(response.getBookPriceStandard());
        book.setBookPubDate(response.getBookPubDate());
        return book;
    }

    private void connectCategoriesToBook(Book book, String categoryName) {
        if (categoryName != null && !categoryName.isBlank()) {
            Category category = findOrCreateCategory(categoryName);
            book.addCategory(category);
        } else {
            System.out.println("카테고리 이름이 비어있어 연결하지 않음.");
        }
    }

    private Publisher findOrCreatePublisher(String publisherName) {
        if (publisherName == null || publisherName.isBlank()) {
            throw new IllegalArgumentException("출판사 이름이 비어있습니다.");
        }
        return publisherRepository.findByPublisherName(publisherName)
                .orElseGet(() -> {
                    Publisher publisher = new Publisher();
                    publisher.setPublisherName(publisherName);
                    return publisherRepository.save(publisher);
                });
    }

    private Category findOrCreateCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setCategoryName(categoryName);
                    category.setCategoryDepth(1); // 기본 Depth 설정
                    return categoryRepository.save(category);
                });
    }
}
