package com.nhnacademy.book.book.service.mapping;

import com.nhnacademy.book.book.dto.response.aladin.AladinResponse;
import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.exception.BookAlreadyExistsException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.book.service.category.ApiCategoryService;
import com.nhnacademy.book.book.service.image.ImageService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class MappingService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final ImageService imageService;
    private final ApiCategoryService categoryService;
    private final SellingBookRepository sellingBookRepository;

    public MappingService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository,
                          ImageService imageService, ApiCategoryService categoryService, SellingBookRepository sellingBookRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.imageService = imageService;
        this.categoryService = categoryService;
        this.sellingBookRepository = sellingBookRepository;
    }

    /**
     * 개별 책 데이터를 처리하고 저장합니다.
     * @param bookData 알라딘 API에서 가져온 개별 책 데이터
     * @return 중복 데이터 여부 (중복: false, 처리 성공: true)
     */
    @Transactional
    public boolean processBookData(AladinResponse bookData) {
        try {
            // 중복 데이터 체크
            if (bookRepository.existsByBookIsbn13(bookData.getIsbn13())) {
                log.info("이미 존재하는 도서: ISBN13 = {}", bookData.getIsbn13());
                return false;
            }

            // Book 매핑 및 저장
            Book book = mapToBookEntity(bookData);
            bookRepository.save(book);
            log.info("저장된 책: {}", book.getBookTitle());

            // 카테고리 저장 및 연결
            if (bookData.getCategoryName() != null) {
                categoryService.saveBookCategory(book, bookData.getCategoryName());
                bookRepository.save(book); // Book 저장 (카테고리 연결 후 다시 저장)
            }

            // 이미지 저장
            imageService.saveBookImage(book, bookData.getCover());

            // SellingBook 생성 및 저장
            SellingBook sellingBook = createSellingBook(book, bookData);
            sellingBookRepository.save(sellingBook);
            log.info("저장된 판매책: {}", sellingBook.getSellingBookId());

            return true; // 처리 성공
        } catch (BookAlreadyExistsException  e) {
            log.error("이미 있는 책: ISBN13 = {}, 이유: {}", bookData.getIsbn13(), e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("예기치 못한 오류 발생: ISBN13 = {}, 오류 메시지: {}", bookData.getIsbn13(), e.getMessage(), e);
            return false;
        }
    }


    /**
     * 알라딘 API 응답 데이터를 Book 엔티티로 매핑합니다.
     * @param response 알라딘 API에서 가져온 책 데이터
     * @return Book 엔티티 객체
     */
    public Book mapToBookEntity(AladinResponse response) {
        Book book = new Book();

        // 기본 정보 설정
        book.setBookTitle(response.getTitle());
        book.setBookDescription(response.getDescription());
        book.setBookIndex(response.getDescription());
        book.setBookIsbn13(response.getIsbn13());
        book.setBookPriceStandard(BigDecimal.valueOf(response.getPriceStandard()));

        // 출판사 매핑
        Publisher publisher = findOrCreatePublisher(response.getPublisher());
        book.setPublisher(publisher);

        // 저자 매핑
        if (response.getAuthor() != null) {
            for (String authorName : response.getAuthor().split(",")) {
                Author author = findOrCreateAuthor(authorName.trim());
                book.addAuthor(author);
            }
        }
        // 날짜 설정
        try {
            book.setBookPubDate(LocalDate.parse(response.getPubDate(), DateTimeFormatter.ISO_DATE));
        } catch (Exception e) {
            log.debug("날짜 파싱 실패: {}", response.getPubDate());

            book.setBookPubDate(LocalDate.now()); //현재 날짜로 설정
        }

        return book;
    }

    /**
     * SellingBook 엔티티를 생성합니다.
     * @param book Book 엔티티
     * @param bookData 알라딘 API 데이터
     * @return 생성된 SellingBook 엔티티
     */
    private SellingBook createSellingBook(Book book, AladinResponse bookData) {
        SellingBook sellingBook = new SellingBook();
        sellingBook.setBook(book);

        // 가격 검증 및 기본값 설정
        BigDecimal priceStandard = BigDecimal.valueOf(bookData.getPriceStandard());
        if (priceStandard.compareTo(BigDecimal.ZERO) <= 0) { // 가격이 0 이하인 경우
            log.warn("잘못된 가격 정보: ISBN13 = {}, 기본값 1000 적용", bookData.getIsbn13());
            priceStandard = BigDecimal.valueOf(1000); // 기본값 설정
        }

        sellingBook.setSellingBookPrice(priceStandard);

        sellingBook.setSellingBookPackageable(false);
        sellingBook.setSellingBookStock(100);
        sellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        sellingBook.setUsed(false);
        sellingBook.setSellingBookViewCount(0L);
        return sellingBook;
    }

    /**
     * 출판사를 찾거나 새로 생성하여 반환합니다.
     * @param publisherName 출판사 이름
     * @return Publisher 엔티티 객체
     */
    private Publisher findOrCreatePublisher(String publisherName) {
        return publisherRepository.findByPublisherName(publisherName)
                .orElseGet(() -> {
                    Publisher publisher = new Publisher();
                    publisher.setPublisherName(publisherName);
                    return publisherRepository.save(publisher);
                });
    }

    /**
     * 저자를 찾거나 새로 생성하여 반환합니다.
     * @param authorName 저자 이름
     * @return Author 엔티티 객체
     */
    private Author findOrCreateAuthor(String authorName) {
        return authorRepository.findByAuthorName(authorName)
                .orElseGet(() -> {
                    Author author = new Author();
                    author.setAuthorName(authorName);
                    return authorRepository.save(author);
                });
    }
}
