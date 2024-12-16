package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SellingBookRepositoryTest {

    @Autowired
    private SellingBookRepository sellingBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    private SellingBook testSellingBook;

    @BeforeEach
    void setUp() {
        // Publisher 생성
        Publisher publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");
        publisher = publisherRepository.save(publisher); // 저장

        // Book 생성
        Book testBook = new Book(
                publisher,
                "Test Book",
                "Index Content",
                "Description Content",
                LocalDate.of(2023, 1, 1),
                new BigDecimal("19.99"),
                "1234567890",
                "1234567890123"
        );
        testBook = bookRepository.save(testBook); // 저장

        // SellingBook 생성
        testSellingBook = new SellingBook();
        testSellingBook.setBook(testBook); // 관계 설정
        testSellingBook.setSellingBookPrice(new BigDecimal("29.99"));
        testSellingBook.setSellingBookPackageable(true);
        testSellingBook.setSellingBookStock(100);
        testSellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        testSellingBook.setUsed(false);
        testSellingBook.setSellingBookViewCount(0L);
    }

    @Test
    @DisplayName("SellingBook 저장 테스트")
    void testSaveSellingBook() {
        // 저장
        SellingBook savedSellingBook = sellingBookRepository.save(testSellingBook);

        // 검증
        assertThat(savedSellingBook.getSellingBookId()).isNotNull();
        assertThat(savedSellingBook.getSellingBookPrice()).isEqualTo(new BigDecimal("29.99"));
        assertThat(savedSellingBook.getSellingBookStock()).isEqualTo(100);
        assertThat(savedSellingBook.getBook().getBookTitle()).isEqualTo("Test Book");
    }
}
