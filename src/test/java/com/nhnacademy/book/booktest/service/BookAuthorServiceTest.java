package com.nhnacademy.book.booktest.service;


import com.nhnacademy.book.book.dto.request.BookAuthorRequestDto;
import com.nhnacademy.book.book.dto.response.AuthorResponseDto;
import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.elastic.repository.BookAuthorSearchRepository;
import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookAuthor;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.exception.AuthorIdNotFoundException;
import com.nhnacademy.book.book.exception.BookAuthorNotFoundException;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.repository.BookAuthorRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.service.Impl.BookAuthorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BookAuthorServiceTest {

    @InjectMocks
    private BookAuthorService bookAuthorService;

    @Mock
    private BookAuthorRepository bookAuthorRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookAuthorSearchRepository bookAuthorSearchRepository;

    @Mock
    private BookRepository bookRepository;

    private Publisher publisher;

    List<Book> books = new ArrayList<>();
    List<Author> authors = new ArrayList<>();

    Book book1;
    Book book2;
    Author author1;
    Author author2;


    @BeforeEach
    public void setUp() {
        publisher = new Publisher("Test Publisher");
        publisher.setPublisherId(1L);

        book1 = new Book(
                publisher,
                "Find Book Title1",
                "Book Index1",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book1.setBookId(1L);
        book2 = new Book(
                publisher,
                "Find Book Title2",
                "Book Index2",
                "Book Description1",
                LocalDate.of(2023, 12, 15),
                new BigDecimal("15.99"),
                "1234567890123453"
        );
        book2.setBookId(2L);

        author1 = new Author();
        author1.setAuthorId(1L);
        author1.setAuthorName("test author1");

        author2 = new Author();
        author2.setAuthorId(2L);
        author2.setAuthorName("test author2");

        authors.add(author1);
        authors.add(author2);

        books.add(book1);
        books.add(book2);
    }



    @Test
    void createBookAuthor() {
        BookAuthorRequestDto bookAuthorRequestDto = new BookAuthorRequestDto();
        bookAuthorRequestDto.setAuthorId(1L);
        bookAuthorRequestDto.setBookId(1L);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book1));
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.ofNullable(author1));
        Mockito.when(bookAuthorRepository.save(any(BookAuthor.class)))
                .thenAnswer(invocation -> {
                    BookAuthor saved = invocation.getArgument(0);
                    saved.setId(10L);
                    return saved;
                });

        bookAuthorService.createBookAuthor(bookAuthorRequestDto);

        // ArgumentCaptor로 save 호출 시 전달된 객체를 캡처
        ArgumentCaptor<BookAuthor> captor = ArgumentCaptor.forClass(BookAuthor.class);
        Mockito.verify(bookAuthorRepository, Mockito.times(1)).save(captor.capture());

        BookAuthor captured = captor.getValue();

        assertEquals(captured.getAuthor().getAuthorId(), 1L);
        assertEquals(captured.getBook().getBookId(), 1L);
    }

    @Test
    void createBookAuthor_AuthorIdException() {
        BookAuthorRequestDto bookAuthorRequestDto = new BookAuthorRequestDto();
        bookAuthorRequestDto.setAuthorId(null);
        bookAuthorRequestDto.setBookId(1L);

        assertThrows(AuthorIdNotFoundException.class, () -> bookAuthorService.createBookAuthor(bookAuthorRequestDto));

    }

    @Test
    void createBookAuthor_BookIdException() {
        BookAuthorRequestDto bookAuthorRequestDto = new BookAuthorRequestDto();
        bookAuthorRequestDto.setAuthorId(1L);
        bookAuthorRequestDto.setBookId(null);

        assertThrows(BookNotFoundException.class, () -> bookAuthorService.createBookAuthor(bookAuthorRequestDto));

    }
    @Test
    void createBook_BookException() {
        BookAuthorRequestDto bookAuthorRequestDto = new BookAuthorRequestDto();
        bookAuthorRequestDto.setAuthorId(1L);
        bookAuthorRequestDto.setBookId(1L);


        assertThrows(BookNotFoundException.class, () -> bookAuthorService.createBookAuthor(bookAuthorRequestDto));

    }

    @Test
    void createBook_AuthorException() {
        BookAuthorRequestDto bookAuthorRequestDto = new BookAuthorRequestDto();
        bookAuthorRequestDto.setAuthorId(1L);
        bookAuthorRequestDto.setBookId(1L);

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book1));

        assertThrows(AuthorIdNotFoundException.class, () -> bookAuthorService.createBookAuthor(bookAuthorRequestDto));

    }

    @Test
    void deleteBookAuthor() {

        Mockito.when(bookAuthorRepository.existsById(anyLong())).thenReturn(true);

        bookAuthorService.deleteBookAuthor(anyLong());

        Mockito.verify(bookAuthorRepository, Mockito.times(1)).deleteById(anyLong());
        Mockito.verify(bookAuthorSearchRepository, Mockito.times(1)).deleteById(anyLong());

    }

    @Test
    void deleteBookAuthor_BookAuthorException() {

        Mockito.when(bookAuthorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(BookAuthorNotFoundException.class, () -> bookAuthorService.deleteBookAuthor(anyLong()));

    }

    @Test
    void findBooksByAuthorId(){

        Mockito.when(bookAuthorRepository.findBooksByAuthorId(anyLong())).thenReturn(books);
        Mockito.when(authorRepository.existsById(anyLong())).thenReturn(true);

        List<BookResponseDto> bookResponseDtos = bookAuthorService.findBooksByAuthorId(1L);




        assertEquals(2, bookResponseDtos.size());
        Mockito.verify(bookAuthorRepository, Mockito.times(1)).findBooksByAuthorId(anyLong());

        for(BookResponseDto bookResponseDto : bookResponseDtos){
            log.info(bookResponseDto.getBookTitle());
        }
    }

    @Test
    void findBooksByAuthorId_AuthorNotFoundException() {

        Mockito.when(authorRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(AuthorIdNotFoundException.class, () -> bookAuthorService.findBooksByAuthorId(anyLong()));

    }

    @Test
    void findAuthorsByBookId() {
        Mockito.when(bookAuthorRepository.findAuthorsByBookId(anyLong())).thenReturn(authors);
        Mockito.when(bookRepository.existsById(anyLong())).thenReturn(true);

        List<AuthorResponseDto> authorResponseDtos = bookAuthorService.findAuthorsByBookId(1L);

        assertEquals(2, authorResponseDtos.size());
        Mockito.verify(bookAuthorRepository, Mockito.times(1)).findAuthorsByBookId(anyLong());
        for(AuthorResponseDto authorResponseDto : authorResponseDtos){
            log.info(authorResponseDto.getAuthorName());
        }

    }


    @Test
    void findAuthorsByBookId_BookNotFoundException() {
        Mockito.when(bookRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookAuthorService.findAuthorsByBookId(anyLong()));
    }
}
