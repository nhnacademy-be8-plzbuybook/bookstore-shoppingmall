package com.nhnacademy.book.booktest;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
public class BookTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Publisher publisher;
    private Category category;
    @BeforeEach
    public void setUp() {
        // Publisher 생성 및 저장
        publisher = new Publisher("Test Publisher");
        publisherRepository.save(publisher);

        Category rootCategory = new Category("국내도서", 1, null);
        categoryRepository.save(rootCategory);  // 루트 카테고리 저장

        Category subCategory1 = new Category("종교/역학", 2, rootCategory);
        categoryRepository.save(subCategory1);  // 첫 번째 자식 카테고리 저장

        Category subCategory2 = new Category("기독교(개신교)", 3, subCategory1);
        categoryRepository.save(subCategory2);  // 두 번째 자식 카테고리 저장

        Category subCategory3 = new Category("기독교(개신교) 신앙생활", 4, subCategory2);
        categoryRepository.save(subCategory3);  // 세 번째 자식 카테고리 저장

        Category subCategory4 = new Category("사랑/결혼", 5, subCategory3);
        categoryRepository.save(subCategory4);  // 네 번째 자식 카테고리 저장

        // 카테고리 테스트용으로 마지막 카테고리 가져오기
        category = subCategory4;

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

        // 카테고리 설정
        book.getCategories().add(category);  // 앞서 설정한 마지막 카테고리를 책에 추가

        // Book 엔티티 저장
        Book savedBook = bookRepository.save(book);

        // Assertions
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getBookId()).isNotNull();
        assertThat(savedBook.getBookTitle()).isEqualTo("Test Book Title");
        assertThat(savedBook.getPublisher().getPublisherName()).isEqualTo("Test Publisher");
        assertThat(savedBook.getCategories()).contains(category);

        // DB에서 해당 book을 다시 조회하여 확인
        Book foundBook = bookRepository.findById(savedBook.getBookId()).orElse(null);
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getBookId()).isEqualTo(savedBook.getBookId());
        assertThat(foundBook.getCategories()).contains(category);  // 카테고리 포함 여부 확인

        log.info("{},{},{},{},{}", foundBook.getBookId(), foundBook.getCategories(), foundBook.getBookTitle(), foundBook.getPublisher().getPublisherName(), foundBook.getBookIsbn13());

        // DB에서 해당 카테고리를 다시 조회하여 확인
        Category foundCategory = categoryRepository.findById(category.getCategoryId()).orElse(null);
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getCategoryName()).isEqualTo(category.getCategoryName());
    }


}
