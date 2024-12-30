package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.BookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.PublisherNotFoundException;
import com.nhnacademy.book.book.repository.BookImageRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.PublisherRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private BookSearchRepository bookSearchRepository;

    @Mock
    private BookImageRepository imageRepository;

    @Mock
    private BookImageRepository bookImageRepository; // BookImageRepository 추가

    @Mock
    private SellingBookRepository sellingBookRepository;

    @Mock
    private Publisher publisher;

    List<Book> books = new ArrayList<>();

    @BeforeEach
    void setUp() {
        publisher = new Publisher("Test Publisher");
        publisher.setPublisherId(1L);

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
        book2.setBookId(2L);
        Book book3 = new Book(
                publisher,
                "Find Book Title3",
                "Book Index3",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book3.setBookId(3L);


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
        Mockito.when(publisherRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(publisherRepository.findById(anyLong())).thenReturn(Optional.ofNullable(publisher));

        bookService.registerBook(bookRegisterDto);
        Mockito.verify(bookRepository, Mockito.times(1)).save(any());

    }

    @Test
    void registerBook_BookNotFoundException(){

        assertThrows(BookNotFoundException.class, () -> bookService.registerBook(null));
    }

    @Test
    void registerBook_shouldThrowPublisherNotFoundException_whenPublisherNotFound() {

        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setPublisherId(1L);

        Mockito.when(publisherRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(PublisherNotFoundException.class, () -> bookService.registerBook(bookRegisterDto));
    }
    @Test
    void registerBook_shouldThrowPublisherNotFoundException_whenPublisherNameIsEmpty() {
        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setPublisherId(1L);

        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);
        publisher.setPublisherName("");

        Mockito.when(publisherRepository.existsById(anyLong())).thenReturn(true);

        Mockito.when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));

        assertThrows(PublisherNotFoundException.class, () -> bookService.registerBook(bookRegisterDto));
    }

    @Test
    void registerBook_shouldThrowPublisherNotFoundException_whenPublisherIdIsNull() {
        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setPublisherId(1L);

        Publisher publisher = new Publisher();
        publisher.setPublisherId(null);
        publisher.setPublisherName("Valid Publisher");

        Mockito.when(publisherRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));

        assertThrows(PublisherNotFoundException.class, () -> bookService.registerBook(bookRegisterDto));
    }

    @Test
    void getAllBooks() {

        Mockito.when(bookRepository.findAll()).thenReturn(books);
        bookService.getAllBooks();
        Mockito.verify(bookRepository, Mockito.times(1)).findAll();

    }

    @Test
    void getBookDetail(){

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.of(books.get(0)));
        BookDetailResponseDto bookDetailResponseDto = bookService.getBookDetail(anyLong());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(anyLong());

    }

    @Test
    void getBookDetail_BookNotFoundException(){

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookDetail(anyLong()));

    }

    @Test
    void deleteBook() {
        Mockito.when(bookRepository.existsById(anyLong())).thenReturn(true);
        bookService.deleteBook(anyLong());
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(any());
        Mockito.verify(bookSearchRepository, Mockito.times(1)).deleteById(any());
    }

    @Test
    void deleteBook_BookNotFoundException(){
        Mockito.when(bookRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(anyLong()));
    }

    @Test
    void updateBook() {
        BookRegisterDto bookRegisterDto = new BookRegisterDto();
        bookRegisterDto.setPublisherId(1L);
        bookRegisterDto.setBookTitle("test");
        bookRegisterDto.setBookIndex("test index");
        bookRegisterDto.setBookDescription("test description");
        bookRegisterDto.setBookPubDate(LocalDate.of(2023, 12, 15));
        bookRegisterDto.setBookPriceStandard(BigDecimal.valueOf(15.99));
        bookRegisterDto.setBookIsbn13("1234567890");


        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(books.getFirst()));
        bookService.updateBook(books.getFirst().getBookId(), bookRegisterDto);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(anyLong());

    }

    @Test
    void updateBook_BookNotFoundException(){
        BookRegisterDto bookRegisterDto = new BookRegisterDto();

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(anyLong(), bookRegisterDto));
    }


}

