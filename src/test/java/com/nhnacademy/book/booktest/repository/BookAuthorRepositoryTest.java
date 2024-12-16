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
        book = bookRepository.save(book);

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
    void findBooksByAuthorIdTest() {
        Book book2 = new Book(
                publisher,
                "Find Book Title",
                "Test Book Index",
                "Test Book Description",
                LocalDate.of(2023, 12, 13),
                new BigDecimal("19.99"),
                "12345678901",
                "1234567890123"
        );
        book2 = bookRepository.save(book2);

        Author author = authorRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Author not found"));

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(book2);
        bookAuthorRepository.save(bookAuthor);

        book2.getBookAuthors().add(bookAuthor);
        bookRepository.save(book2);

        List<Book> books = bookAuthorRepository.findBooksByAuthorId(author.getAuthorId());

        assertThat(books).isNotNull().hasSize(2).contains(book2);

        boolean hasBookWithId1 = books.stream()
                .anyMatch(book -> book.getBookId().equals(1L));
        assertTrue(hasBookWithId1);
    }

    @Test
    void findBooksByNonExistentAuthorIdTest() {
        List<Book> books = bookAuthorRepository.findBooksByAuthorId(999L);

        assertThat(books).isEmpty();
    }

    @Test
    void findAuthorsByNonExistentBookIdTest() {
        List<Author> authors = bookAuthorRepository.findAuthorsByBookId(999L);

        assertThat(authors).isEmpty();
    }
}
