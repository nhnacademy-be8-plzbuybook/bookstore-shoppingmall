package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.BookAuthorRequestDto;
import com.nhnacademy.book.book.dto.request.BookRegisterRequestDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;


    @Mock
    private BookImageRepository bookImageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookAuthorService bookAuthorService;

    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private BookCategoryService bookCategoryService;

    @BeforeEach
    @DisplayName("Mock 객체 초기화")
    void setUp() {
        MockitoAnnotations.openMocks(this);


        Book testBook = new Book(1L, "Test Book");

        // Mock 동작 설정

        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book(1L, "Mock Book")));
        when(bookRepository.existsById(1L)).thenReturn(true);

        when(publisherRepository.findByPublisherName(anyString()))
                .thenReturn(Optional.of(new Publisher("Mock Publisher")));

        when(categoryRepository.findByCategoryId(anyLong()))
                .thenReturn(Optional.of(new Category("Mock Category")));

        when(authorRepository.findByAuthorName(anyString()))
                .thenReturn(Optional.of(new Author("Mock Author")));

        when(bookRepository.existsById(anyLong())).thenReturn(true);
    }

    @Test
    @DisplayName("책 세부 정보 조회 성공")
    void shouldReturnBookDetail_WhenBookExists() {
        // Given
        Long bookId = 1L;
        Publisher publisher = new Publisher("Test Publisher");
        Book book = new Book(
                bookId,
                publisher,
                "Test Book",
                "Index",
                "Description",
                LocalDate.of(2023, 1, 1),
                BigDecimal.valueOf(15000),
                "1234567890123"
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookImageRepository.findByBook(book))
                .thenReturn(Optional.of(new BookImage(book, "http://example.com/image.jpg")));

        // When
        BookDetailResponseDto result = bookService.getBookDetail(bookId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBookTitle()).isEqualTo("Test Book");
        assertThat(result.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        verify(bookRepository, times(1)).findById(bookId);
    }


    @Test
    @DisplayName("존재하지 않는 책 세부 정보 조회 시 예외 발생")
    void shouldThrowException_WhenBookNotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookDetail(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("책 등록 성공")
    void shouldRegisterBook_WhenValidRequest() {
        BookRegisterRequestDto requestDto = new BookRegisterRequestDto(
                1L, "Test Book", "Index", "Description",
                LocalDate.of(2023, 1, 1), BigDecimal.valueOf(15000),
                "1234567890123", "Test Publisher",
                "http://example.com/image.jpg",
                List.of(new CategoryResponseDto(1L, "Fiction", 1, null, null)),
                List.of("한강")
        );

        bookService.registerBook(requestDto);

        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookImageRepository, times(1)).save(any(BookImage.class));
        verify(bookAuthorService, times(1)).createBookAuthor(any(BookAuthorRequestDto.class));
    }

    @Test
    void testDeleteBook_BookNotFound() {
        // Given
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository, times(1)).existsById(bookId);
    }

    @Test
    @DisplayName("존재하지 않는 책 삭제 시 예외 발생")
    void shouldThrowException_WhenDeletingNonExistentBook() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
    }

    @Test
    @DisplayName("책 정보 수정 성공")
    void shouldUpdateBook_WhenValidRequest() {
        Long bookId = 1L;
        BookRegisterRequestDto requestDto = new BookRegisterRequestDto(
                bookId, "Updated Title", "Updated Index", "Updated Description",
                LocalDate.of(2023, 1, 1), BigDecimal.valueOf(20000),
                "1234567890123", "Updated Publisher",
                "http://example.com/updated-image.jpg",
                List.of(new CategoryResponseDto(1L, "Fiction", 1, null, null)),
                List.of("Author1", "Author2")
        );

        Book book = new Book();
        book.setBookId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.updateBook(requestDto);

        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
