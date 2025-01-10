package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.dto.request.BookRegisterDto;
import com.nhnacademy.book.book.dto.response.AdminBookAndSellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.elastic.repository.BookSearchRepository;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import com.nhnacademy.book.book.service.Impl.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private BookImageRepository bookImageRepository;

    @Mock
    private SellingBookRepository sellingBookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthorRepository authorRepository;

    private Book testBook;

    private AdminBookAndSellingBookRegisterDto bookAndSellingDto;
    private BookRegisterDto bookUpdateDto;



    @BeforeEach
    public void setUp() {
        Publisher testPublisher = new Publisher();
        testPublisher.setPublisherId(1L);
        testPublisher.setPublisherName("Test Publisher");

        testBook = new Book();
        testBook.setBookId(1L);
        testBook.setBookTitle("Test Book");
        testBook.setBookIndex("1");
        testBook.setBookDescription("Test Description");
        testBook.setBookPubDate(LocalDate.parse("2025-01-01"));
        testBook.setBookPriceStandard(BigDecimal.valueOf(1000));
        testBook.setBookIsbn13("1234567890123");
        testBook.setPublisher(testPublisher);

        bookAndSellingDto = new AdminBookAndSellingBookRegisterDto();
        bookAndSellingDto.setPublisher("Test Publisher");
        bookAndSellingDto.setBookTitle("Test Book");
        bookAndSellingDto.setBookIndex("1");
        bookAndSellingDto.setBookDescription("Test Description");
        bookAndSellingDto.setBookPubDate(LocalDate.parse("2025-01-01"));
        bookAndSellingDto.setBookPriceStandard(BigDecimal.valueOf(1000));
        bookAndSellingDto.setBookIsbn13("1234567890123");
        bookAndSellingDto.setImageUrl("http://example.com/image.jpg");
        bookAndSellingDto.setCategories(List.of("Fiction", "Adventure"));
        bookAndSellingDto.setAuthors(List.of("Author1", "Author2"));
        bookAndSellingDto.setSellingBookPrice(BigDecimal.valueOf(1500));
        bookAndSellingDto.setSellingBookStock(10);
        bookAndSellingDto.setSellingBookPackageable(true);
        bookAndSellingDto.setSellingBookStatus("AVAILABLE");

        bookUpdateDto = new BookRegisterDto();
        bookUpdateDto.setBookTitle("Updated Book Title");
        bookUpdateDto.setBookIndex(String.valueOf(2));
        bookUpdateDto.setBookDescription("Updated Description");
        bookUpdateDto.setBookPubDate(LocalDate.parse("2026-01-01"));
        bookUpdateDto.setBookPriceStandard(BigDecimal.valueOf(2000));
        bookUpdateDto.setBookIsbn13("9876543210123");
    }

    @Test
    void existsBook_BookExists() {
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);

        boolean exists = bookService.existsBook(1L);

        assertThat(exists).isTrue();
    }

    @Test
    void existsBook_BookNotFoundException() {
        Mockito.when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.existsBook(1L));
    }

    @Test
    void getAllBooks() {
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(testBook));
        Mockito.when(sellingBookRepository.findByBook(testBook)).thenReturn(null);
        Mockito.when(bookImageRepository.findByBook(testBook)).thenReturn(Optional.empty());

        List<BookDetailResponseDto> books = bookService.getAllBooks();

        assertThat(books).hasSize(1);
        assertThat(books.getFirst().getBookTitle()).isEqualTo("Test Book");
    }

    @Test
    void getBookDetail() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        Mockito.when(bookImageRepository.findByBook(testBook)).thenReturn(Optional.empty());

        BookDetailResponseDto bookDetail = bookService.getBookDetail(1L);

        assertThat(bookDetail.getBookTitle()).isEqualTo("Test Book");
        assertThat(bookDetail.getBookId()).isEqualTo(1L);
    }

    @Test
    void getBookDetail_BookNotFoundException() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookDetail(1L));
    }

    @Test
    void getBookDetailFromElastic() {
        BookDocument bookDocument = new BookDocument();
        bookDocument.setBookId(1L);
        bookDocument.setBookTitle("Test Book");

        Mockito.when(bookSearchRepository.findByBookId(1L)).thenReturn(bookDocument);

        BookDetailResponseDto bookDetail = bookService.getBookDetailFromElastic(1L);

        assertThat(bookDetail.getBookId()).isEqualTo(1L);
        assertThat(bookDetail.getBookTitle()).isEqualTo("Test Book");
    }

    @Test
    void getBookDetailFromElastic_BookNotFoundException() {
        Mockito.when(bookSearchRepository.findByBookId(1L)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookService.getBookDetailFromElastic(1L));
    }

    @Test
    void deleteBook() {
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
        Mockito.verify(bookSearchRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_BookNotFoundException() {
        Mockito.when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }

//    @Test
//    void registerBookAndSellingBook_Success() {
//        Publisher publisher = new Publisher();
//        publisher.setPublisherId(1L);
//        publisher.setPublisherName("Test Publisher");
//
//        Mockito.when(publisherRepository.findByPublisherName("Test Publisher"))
//                .thenReturn(Optional.of(publisher));
//        Mockito.when(categoryRepository.findByCategoryName(Mockito.anyString()))
//                .thenAnswer(invocation -> Optional.empty());
//        Mockito.when(authorRepository.findByAuthorName(Mockito.anyString()))
//                .thenAnswer(invocation -> Optional.empty());
//
//        bookService.registerBookAndSellingBook(bookAndSellingDto);
//
//        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(Book.class));
//        Mockito.verify(bookImageRepository, Mockito.times(1)).save(Mockito.any(BookImage.class));
//        Mockito.verify(sellingBookRepository, Mockito.times(1)).save(Mockito.any(SellingBook.class));
//        Mockito.verify(categoryRepository, Mockito.times(2)).save(Mockito.any(Category.class));
//        Mockito.verify(authorRepository, Mockito.times(2)).save(Mockito.any(Author.class));
//    }

//    @Test
//    void registerBookAndSellingBook_PublisherNotFound() {
//        Mockito.when(publisherRepository.findByPublisherName("Test Publisher"))
//                .thenReturn(Optional.empty());
//
//        assertThrows(IllegalArgumentException.class, () -> bookService.registerBookAndSellingBook(bookAndSellingDto));
//    }

    @Test
    void updateBook_Success() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        bookService.updateBook(1L, bookUpdateDto);

        assertThat(testBook.getBookTitle()).isEqualTo("Updated Book Title");
        assertThat(testBook.getBookIndex()).isEqualTo("2");
        Mockito.verify(bookRepository, Mockito.times(1)).save(testBook);
    }

    @Test
    void updateBook_BookNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(1L, bookUpdateDto));
    }
}
