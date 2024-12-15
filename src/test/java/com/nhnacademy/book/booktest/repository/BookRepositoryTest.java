package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

        category = subCategory4;

        //delete,update 용 book 생성?
        Book book = new Book(
                publisher,                              // Publisher 설정
                "Test Book Title",                      // 제목
                "Test Book Index",                      // 목차
                "Test Book Description",                // 설명
                LocalDate.of(2023, 12, 13),             // 출판일
                new BigDecimal("19.99"),                // 가격
                "1234567890122",                        // ISBN
                "1234567890123451"                      // ISBN-13
        );

        Author author = new Author();
        author.setAuthorName("test author");
        author = authorRepository.save(author);
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book);
        bookAuthorRepository.save(bookAuthor);
        book.getBookAuthors().add(bookAuthor);



        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(category);
        bookCategory.setBook(book);
        bookCategoryRepository.save(bookCategory);

        book.getBookCategories().add(bookCategory);

        bookRepository.save(book);

    }

    @Test
    void bookCreateTest() {
        Book book = new Book(
                publisher,                              // Publisher 설정
                "Book Title2",                          // 제목
                "Book Index2",                          // 목차
                "Book Description2",                    // 설명
                LocalDate.of(2023, 12, 13),             // 출판일
                new BigDecimal("19.99"),                // 가격
                "1234567890",                           // ISBN
                "1234567890123"                         // ISBN-13
        );

        Author author = new Author();
        author.setAuthorName("test author");
        author = authorRepository.save(author);
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book);
        bookAuthorRepository.save(bookAuthor);

        book.getBookAuthors().add(bookAuthor);

        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(category);
        bookCategory.setBook(book);
        bookCategoryRepository.save(bookCategory);

        book.getBookCategories().add(bookCategory);


        Book savedBook = bookRepository.save(book);


        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getBookId()).isNotNull();
        assertThat(savedBook.getBookTitle()).isEqualTo("Book Title2");
        assertThat(savedBook.getPublisher().getPublisherName()).isEqualTo("Test Publisher");
        assertThat(savedBook.getBookCategories()).hasSize(1); // BookCategory가 1개로 연결된 상태
        assertThat(savedBook.getBookCategories().getFirst().getCategory()).isEqualTo(category);
        assertThat(savedBook.getBookAuthors().getFirst()).isEqualTo(bookAuthor);
    }


    @Test
    void findByBookIsbnTest(){
        Book book = bookRepository.findByBookIsbn("1234567890122");

        assertThat(book).isNotNull();
        assertThat(book.getBookId()).isEqualTo(1);

    }

    @Test
    void findByBookTitleTest(){

        Book book = new Book(
                publisher,                              // Publisher 설정
                "Find Book Title",                      // 제목
                "Book Index2",                      // 목차
                "Book Description2",                // 설명
                LocalDate.of(2023, 12, 13),             // 출판일
                new BigDecimal("19.99"),                // 가격
                "1234567892",                        // ISBN
                "1234567890125"                      // ISBN-13
        );
        bookRepository.save(book);

        Book book2 = new Book(
                publisher,                              // Publisher 설정
                "Find Book Title",                      // 제목
                "Test Book Index",                      // 목차
                "Test Book Description",                // 설명
                LocalDate.of(2023, 12, 13),             // 출판일
                new BigDecimal("19.99"),                // 가격
                "12345678901",                        // ISBN
                "1234567890123"                      // ISBN-13
        );
        Book savedBook2 = bookRepository.save(book2);

        List<Book> books = bookRepository.findByBookTitle("Find Book Title");

        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
        assertThat(books).contains(book, book2);
    }




}
