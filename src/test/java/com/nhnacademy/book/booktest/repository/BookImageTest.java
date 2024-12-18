package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookImageTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    private Book book;
    private Publisher publisher;

    @BeforeEach
    void setUp() {
        // Publisher 객체 초기화
        publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");
        publisherRepository.save(publisher);  // Publisher를 먼저 저장

        // Book 객체 초기화
        book = new Book();
        book.setBookTitle("Test Book");
        book.setBookIsbn13("1234567890123");
        book.setBookPriceStandard(BigDecimal.valueOf(10000.0));  // book_price_standard에 BigDecimal로 값 설정
        book.setBookPubDate(LocalDate.now());
        book.setBookDescription("Test Description");
        book.setBookIndex("Test Index");
        book.setPublisher(publisher);  // Publisher를 설정
        bookRepository.save(book);  // Book 객체 저장
    }


}
