package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Publisher publisher;

    private Category category;

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

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getBookId()).isNotNull();
        assertThat(savedBook.getBookTitle()).isEqualTo("Book Title2");
        assertThat(savedBook.getPublisher().getPublisherName()).isEqualTo("Test Publisher");
    }

    @Test
    void findByBookIsbnTest() {
        // ISBN으로 책 검색
        Book book = bookRepository.findByBookIsbn13("1234567890123451");

        assertThat(book).isNotNull();
        assertThat(book.getBookTitle()).isEqualTo("Test Book Title");
        assertThat(book.getBookId()).isNotNull();
    }

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

        assertThat(books).isNotNull();
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getBookTitle).containsOnly("Find Book Title");
    }

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

        // 제목에 "Find Me" 포함된 책 검색
        List<Book> books = bookRepository.findByBookTitleContaining("Find Me");

        assertThat(books).isNotNull();
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getBookTitle()).isEqualTo("Find Me Book Title");
    }
}
