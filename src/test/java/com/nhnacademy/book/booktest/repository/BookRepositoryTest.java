package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import com.nhnacademy.book.config.ElasticsearchConfig;
import com.nhnacademy.book.config.JpaRepositoryConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaRepositoryConfig.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Publisher publisher;

    private Category category;

    // 테스트 전에 실행되는 메서드: 각 테스트가 실행되기 전에 데이터베이스에 필요한 데이터를 세팅합니다.
    @BeforeEach
    void setUp() {
        // Publisher 저장
        publisher = new Publisher("Test Publisher");
        publisherRepository.save(publisher);

        // Category 저장
        Category rootCategory = new Category("국내도서", 1, null);
        categoryRepository.save(rootCategory);

        category = new Category("기독교(개신교)", 2, rootCategory);
        categoryRepository.save(category);

        // Book 저장
        Book book = new Book(
                publisher,
                "Test Book Title",
                "Test Book Index",
                "Test Book Description",
                LocalDate.of(2023, 12, 13),
                new BigDecimal("19.99"),
                "1234567890123451"
        );

        // 카테고리와 관계 설정
        book.addCategory(category);
        bookRepository.save(book);
    }

    // 책 생성 테스트
    @Test
    void bookCreateTest() {
        // 새로운 책 생성
        Book newBook = new Book(
                publisher,
                "Book Title2",
                "Book Index2",
                "Book Description2",
                LocalDate.of(2023, 12, 14),
                new BigDecimal("29.99"),
                "1234567890123452"
        );

        // 책 저장
        Book savedBook = bookRepository.save(newBook);

        log.info("Saved Book: {}", savedBook);  // 책 저장 후 로그 출력

        // 저장된 책 검증
        assertNotNull(savedBook);
        assertNotNull(savedBook.getBookId());
        assertEquals("Book Title2", savedBook.getBookTitle());
        assertEquals("Test Publisher", savedBook.getPublisher().getPublisherName());
    }

    // ISBN으로 책을 찾는 테스트
    @Test
    void findByBookIsbnTest() {
        // ISBN으로 책 검색
        Book book = bookRepository.findByBookIsbn13("1234567890123451");

        log.info("Found Book by ISBN: {}", book);  // ISBN으로 책 찾은 후 로그 출력

        // 책 검증
        assertNotNull(book);
        assertEquals("Test Book Title", book.getBookTitle());
        assertNotNull(book.getBookId());
    }

    // 제목으로 책을 찾는 테스트
    @Test
    void findByBookTitleTest() {
        // 추가 책 저장
        Book book1 = new Book(
                publisher,
                "Find Book Title",
                "Book Index1",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        bookRepository.save(book1);

        Book book2 = new Book(
                publisher,
                "Find Book Title",
                "Book Index2",
                "Book Description2",
                LocalDate.of(2023, 12, 16),
                new BigDecimal("18.99"),
                "1234567890123454"
        );
        bookRepository.save(book2);

        // 제목으로 책 검색
        List<Book> books = bookRepository.findByBookTitle("Find Book Title");

        log.info("Found Books by Title: {}", books);  // 제목으로 책 찾은 후 로그 출력

        // 책 검증
        assertNotNull(books);
        assertEquals(2, books.size());
        for (Book b : books) {
            assertEquals("Find Book Title", b.getBookTitle());
        }
    }

    // 제목에 특정 문자열이 포함된 책을 찾는 테스트
    @Test
    void findByBookTitleContainingTest() {
        // 추가 책 저장
        Book book = new Book(
                publisher,
                "Find Me Book Title",
                "Book Index",
                "Book Description",
                LocalDate.of(2023, 12, 17),
                new BigDecimal("12.99"),
                "1234567890123455"
        );
        bookRepository.save(book);

        // 제목에 "Find Me"가 포함된 책 검색
        List<Book> books = bookRepository.findByBookTitleContaining("Find Me");

        log.info("Found Books containing 'Find Me': {}", books);  // 포함된 제목으로 책 찾은 후 로그 출력

        // 책 검증
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Find Me Book Title", books.get(0).getBookTitle());
    }
}
