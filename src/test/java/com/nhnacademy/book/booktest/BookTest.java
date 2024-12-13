package com.nhnacademy.book.booktest;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
public class BookTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher publisher;

    @BeforeEach
    public void setUp() {
        // Publisher 생성 및 저장
        publisher = new Publisher("Test Publisher");
        publisherRepository.save(publisher);
    }

    @Test
    void bookCreateTest() {
        // Book 엔티티 생성
        Book book = new Book(
                publisher,                              // Publisher 설정
                "Test Book Title",                      // 제목
                "Test Book Index",                      // 목차
                "Test Book Description",                // 설명
                LocalDate.of(2023, 12, 13),             // 출판일
                new BigDecimal("19.99"),                // 가격
                "1234567890123",                        // ISBN
                "1234567890123456"                      // ISBN-13
        );

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getBookId()).isNotNull();
        assertThat(savedBook.getBookTitle()).isEqualTo("Test Book Title");
        assertThat(savedBook.getPublisher().getPublisherName()).isEqualTo("Test Publisher");
    }
}
