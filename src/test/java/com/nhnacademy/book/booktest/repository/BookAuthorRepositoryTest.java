package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.repository.BookAuthorRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
public class BookAuthorRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    @Autowired
    private PublisherRepository publisherRepository;


    private Publisher publisher;

    @BeforeEach
    public void setUp() {
        publisher = new Publisher("Test Publisher");
        publisherRepository.save(publisher);


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

        bookRepository.save(book);

    }

    @Test
    void findBooksByAuthorNameTest(){

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

        Author author = authorRepository.findById(1L).get();
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book2);
        bookAuthorRepository.save(bookAuthor);
        book2.getBookAuthors().add(bookAuthor);


        bookRepository.save(book2);

        List<Book> books = bookAuthorRepository.findBooksByAuthorId(1L);

        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
        assertThat(books).contains(book2);

        boolean hasBookWithId1 = books.stream()
                .anyMatch(book -> book.getBookId() == 1L);

        assertTrue(hasBookWithId1);
    }


    @Test
    void findAuthorsByBookId(){
        Author author = new Author();
        author.setAuthorName("test author2");
        authorRepository.save(author);

        Book book = bookRepository.findById(1L).get();

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book);

        bookAuthorRepository.save(bookAuthor);

        List<Author> authors = bookAuthorRepository.findAuthorsByBookId(1L);


        // test 용
        for(Author author1 : authors){
            log.info(author1.toString());
        }

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isEqualTo(2);



    }
}
