//package com.nhnacademy.book.booktest.repository;
//
//import com.nhnacademy.book.book.entity.*;
//import com.nhnacademy.book.book.repository.AuthorRepository;
//import com.nhnacademy.book.book.repository.BookAuthorRepository;
//import com.nhnacademy.book.book.repository.BookRepository;
//import com.nhnacademy.book.book.repository.PublisherRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Slf4j
//@DataJpaTest
////@ActiveProfiles("test")
//public class BookAuthorRepositoryTest {
//
//    @Autowired
//    private BookRepository bookRepository;
//    @Autowired
//    private AuthorRepository authorRepository;
//    @Autowired
//    private BookAuthorRepository bookAuthorRepository;
//    @Autowired
//    private PublisherRepository publisherRepository;
//
//    private Publisher publisher;
//
//    @BeforeEach
//    public void setUp() {
//        // Publisher 저장
//        publisher = new Publisher("Test Publisher");
//        publisher = publisherRepository.save(publisher);
//
//        // Book 저장
//        Book book = new Book(
//                publisher,
//                "Test Book Title",
//                "Test Book Index",
//                "Test Book Description",
//                LocalDate.of(2023, 12, 13),
//                new BigDecimal("19.99"),
//                "1234567890123451"
//        );
//        book = bookRepository.save(book);
//
//        // Author 저장
//        Author author = new Author();
//        author.setAuthorName("test author");
//        author = authorRepository.save(author);
//
//        // BookAuthor 저장
//        BookAuthor bookAuthor = new BookAuthor();
//        bookAuthor.setAuthor(author);
//        bookAuthor.setBook(book);
//        bookAuthor = bookAuthorRepository.save(bookAuthor);
//
//        // 관계 동기화
//        book.getBookAuthors().add(bookAuthor);
//        bookRepository.save(book);
//    }
//
//    @Test
//    void findBooksByAuthorIdTest() {
//        // 추가 Book 저장
//        Book book2 = new Book(
//                publisher,
//                "Find Book Title",
//                "Test Book Index",
//                "Test Book Description",
//                LocalDate.of(2023, 12, 13),
//                new BigDecimal("19.99"),
//                "1234567890123"
//        );
//        book2 = bookRepository.save(book2);
//
//        // Author 조회
//        Author author = authorRepository.findById(1L)
//                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
//
//        // BookAuthor 저장
//        BookAuthor bookAuthor = new BookAuthor();
//        bookAuthor.setAuthor(author);
//        bookAuthor.setBook(book2);
//        bookAuthor = bookAuthorRepository.save(bookAuthor);
//
//        // 관계 동기화
//        book2.getBookAuthors().add(bookAuthor);
//        bookRepository.save(book2);
//
//        // 테스트 실행
//        List<Book> books = bookAuthorRepository.findBooksByAuthorId(author.getAuthorId());
//
//        assertNotNull(books);
//        assertEquals(2, books.size());
//        assertTrue(books.contains(book2));
//    }
//
//    @Test
//    void findBooksByNonExistentAuthorIdTest() {
//        List<Book> books = bookAuthorRepository.findBooksByAuthorId(999L);
//
//        assertNotNull(books);
//        assertTrue(books.isEmpty());
//
//    }
//
//    @Test
//    void findAuthorsByNonExistentBookIdTest() {
//        List<Author> authors = bookAuthorRepository.findAuthorsByBookId(999L);
//
//        assertNotNull(authors);
//        assertTrue(authors.isEmpty());
//
//    }
//}
