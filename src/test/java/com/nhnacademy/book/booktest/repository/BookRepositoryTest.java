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
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookAuthorRepository bookAuthorRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    private Publisher publisher;
    private Category category;

    @BeforeEach
    public void setUp() {
        // Publisher 저장
        publisher = new Publisher("Test Publisher");
        publisherRepository.save(publisher);

        // 카테고리 저장
        Category rootCategory = new Category("국내도서", 1, null);
        categoryRepository.save(rootCategory);

        Category subCategory = new Category("기독교(개신교)", 2, rootCategory);
        categoryRepository.save(subCategory);

        category = subCategory;

        // Book 저장
        Book book = new Book(
                publisher,
                "Test Book Title",
                "Test Book Index",
                "Test Book Description",
                LocalDate.of(2023, 12, 13),
                new BigDecimal("19.99"),
                "1234567890122",
                "1234567890123451"
        );

        // Author와 BookAuthor 관계 설정
        Author author = new Author();
        author.setAuthorName("test author");
        author = authorRepository.save(author);

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book);
        bookAuthorRepository.save(bookAuthor);
        book.getBookAuthors().add(bookAuthor);

        // Category와 BookCategory 관계 설정
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(category);
        bookCategory.setBook(book);
        bookCategoryRepository.save(bookCategory);
        book.getBookCategories().add(bookCategory);

        bookRepository.save(book);
    }

    // 책 생성 테스트
    @Test
    void bookCreateTest() {
        Book newBook = new Book(
                publisher,
                "Book Title2",
                "Book Index2",
                "Book Description2",
                LocalDate.of(2023, 12, 14),
                new BigDecimal("29.99"),
                "1234567890123",
                "1234567890123452"
        );

        Book savedBook = bookRepository.save(newBook);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getBookId()).isNotNull();
        assertThat(savedBook.getBookTitle()).isEqualTo("Book Title2");
        assertThat(savedBook.getPublisher().getPublisherName()).isEqualTo("Test Publisher");
    }

    // ISBN으로 책 찾기 테스트
    @Test
    void findByBookIsbnTest() {
        Book book = bookRepository.findByBookIsbn("1234567890122");

        assertThat(book).isNotNull();
        assertThat(book.getBookTitle()).isEqualTo("Test Book Title");
        assertThat(book.getBookId()).isNotNull();
    }

    // 제목으로 책 찾기 테스트
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
                "1234567890124",
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
                "1234567890125",
                "1234567890123454"
        );
        bookRepository.save(book2);

        // 제목으로 책 조회
        List<Book> books = bookRepository.findByBookTitle("Find Book Title");

        assertThat(books).isNotNull();
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getBookTitle).containsOnly("Find Book Title");
    }

    // 제목 검색 테스트 (제목 포함 여부)
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
                "1234567890126",
                "1234567890123455"
        );
        bookRepository.save(book);

        // 제목에 "Find Me" 포함된 책 조회
        List<Book> books = bookRepository.findByBookTitleContaining("Find Me");

        assertThat(books).isNotNull();
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getBookTitle()).isEqualTo("Find Me Book Title");
    }

    // ISBN 또는 ISBN-13 존재 여부 확인 테스트
    @Test
    void existsByBookIsbnOrBookIsbn13Test() {
        boolean exists = bookRepository.existsByBookIsbnOrBookIsbn13("1234567890122", "1234567890123451");

        assertThat(exists).isTrue();

        boolean notExists = bookRepository.existsByBookIsbnOrBookIsbn13("9999999999999", "8888888888888");

        assertThat(notExists).isFalse();
    }
}
