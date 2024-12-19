package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.BookCategoryRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.CategoryRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookCategoryRepositoryTest {

    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Book book;
    private Book book2;

    @BeforeEach
    void setUp() {
        // 1. Publisher 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher");
        publisherRepository.save(publisher);

        // 2. 카테고리 계층 구조 생성 및 저장
        Category rootCategory = new Category("국내도서", 1, null); // 루트 카테고리
        categoryRepository.save(rootCategory);

        Category subCategory1 = new Category("종교/역학", 2, rootCategory); // 첫 번째 자식 카테고리
        categoryRepository.save(subCategory1);

        Category subCategory2 = new Category("기독교(개신교)", 3, subCategory1); // 두 번째 자식 카테고리
        categoryRepository.save(subCategory2);

        Category anotherCategory = new Category("다른 카테고리", 2, rootCategory); // 별도의 카테고리
        categoryRepository.save(anotherCategory);

        // 3. 책 생성 및 저장
        book = new Book(
                publisher,
                "Test Book Title",
                "Test Book Index",
                "Test Book Description",
                LocalDate.of(2023, 12, 13),
                new BigDecimal("19.99"),
                "1234567890123451"
        );
        bookRepository.save(book);

        book2 = new Book(
                publisher,
                "Test Book Title2",
                "Test Book Index2",
                "Test Book Description2",
                LocalDate.of(2023, 12, 13),
                new BigDecimal("19.99"),
                "1234567890123111"
        );
        bookRepository.save(book2);

        // 4. BookCategory 관계 설정 및 저장
        bookCategoryRepository.save(new BookCategory(book, subCategory2)); // 첫 번째 책, 두 번째 자식 카테고리
        bookCategoryRepository.save(new BookCategory(book, anotherCategory)); // 첫 번째 책, 다른 카테고리
        bookCategoryRepository.save(new BookCategory(book2, subCategory2)); // 두 번째 책, 두 번째 자식 카테고리
    }

    // 테스트 1: BookCategory 데이터 검증
    @Test
    void verifyBookCategoryData() {
        // 모든 BookCategory 엔티티 조회
        List<BookCategory> bookCategories = bookCategoryRepository.findAll();

        // 각 BookCategory 엔티티의 책 ID와 카테고리 ID 출력
        bookCategories.forEach(bookCategory -> {
            System.out.println("Book ID: " + bookCategory.getBook().getBookId());
            System.out.println("Category ID: " + bookCategory.getCategory().getCategoryId());
        });

        // BookCategory 개수 검증
        assertEquals(3, bookCategories.size()); // 총 3개의 관계
    }

    // 테스트 2: 특정 책에 속한 카테고리 조회 (카테고리 1개인 경우)
    @Test
    void testFindCategoriesByBookId_SingleCategory() {
        List<Category> categories = bookCategoryRepository.findCategoriesByBookId(book2.getBookId());

        // 카테고리 개수 검증 (1개만 존재)
        assertEquals(1, categories.size());
        assertEquals("기독교(개신교)", categories.get(0).getCategoryName());
    }

    // 테스트 3: 특정 책에 속한 카테고리 조회 (카테고리 여러 개인 경우)
    @Test
    void testFindCategoriesByBookId_MultipleCategories() {
        List<Category> categories = bookCategoryRepository.findCategoriesByBookId(book.getBookId());

        // 카테고리 개수 검증 (2개 존재)
        assertEquals(2, categories.size());
    }
}
