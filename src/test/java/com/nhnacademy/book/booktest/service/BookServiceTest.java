package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.BookRegisterDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import com.nhnacademy.book.book.service.Impl.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    private Publisher publisher;

    List<Book> books = new ArrayList<>();

    @BeforeEach
    void setUp() {
        publisher = new Publisher("Test Publisher");
//        Mockito.when(publisherRepository.save(Mockito.any(Publisher.class))).thenReturn(publisher);

        Book book1 = new Book(
                publisher,
                "Find Book Title1",
                "Book Index1",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book1.setBookId(1L);

        Book book2 = new Book(
                publisher,
                "Find Book Title2",
                "Book Index2",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book1.setBookId(2L);
        Book book3 = new Book(
                publisher,
                "Find Book Title3",
                "Book Index3",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book1.setBookId(3L);


        books.add(book1);
        books.add(book2);
        books.add(book3);

    }

    @Test
    void registerBook() {
        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setPublisherId(1L);
        bookRegisterDto.setBookTitle("test");
        bookRegisterDto.setBookIndex("test index");
        bookRegisterDto.setBookDescription("test description");
        bookRegisterDto.setBookPubDate(LocalDate.of(2023, 12, 15));
        bookRegisterDto.setBookPriceStandard(BigDecimal.valueOf(15.99));
        bookRegisterDto.setBookIsbn13("1234567890");

        Mockito.when(publisherRepository.findById(anyLong())).thenReturn(Optional.ofNullable(publisher));

        bookService.registerBook(bookRegisterDto);
        Mockito.verify(bookRepository, Mockito.times(1)).save(any());

    }




}
