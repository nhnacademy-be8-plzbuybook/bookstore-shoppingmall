//package com.nhnacademy.book.book.service.Impl;
//
//import com.nhnacademy.book.book.dto.request.BookCategoryRequestDto;
//import com.nhnacademy.book.book.dto.request.BookRegisterRequestDto;
//import com.nhnacademy.book.book.dto.response.BookCategoryResponseDto;
//import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
//import com.nhnacademy.book.book.dto.response.BookRegisterDto;
//import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
//import com.nhnacademy.book.book.entity.*;
//import com.nhnacademy.book.book.exception.BookNotFoundException;
//import com.nhnacademy.book.book.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//class BookServiceTest {
//
//    @InjectMocks
//    private BookService bookService;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private BookAuthorRepository bookAuthorRepository;
//
//    @Mock
//    private BookCategoryService bookCategoryService;
//
//    @Mock
//    private PublisherRepository publisherRepository;
//
//    @Mock
//    private BookImageRepository bookImageRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private AuthorRepository authorRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Mock 설정
//        when(bookCategoryService.createBookCategory(any(BookCategoryRequestDto.class)))
//                .thenReturn(new BookCategoryResponseDto(1L, 1L, "Category Name", 1L, null));
//
//        when(bookImageRepository.findByBook_BookId(anyLong()))
//                .thenReturn(List.of(new BookImage(new Book(), "http://example.com/image.jpg")));
//
//        when(publisherRepository.findByPublisherName(anyString()))
//                .thenReturn(Optional.of(new Publisher("Test Publisher")));
//
//        when(categoryRepository.findByCategoryId(anyLong()))
//                .thenReturn(Optional.of(new Category()));
//
//        when(bookAuthorRepository.findAuthorsByBookId(anyLong()))
//                .thenReturn(List.of(new Author("Author Name")));
//
//        when(authorRepository.findByAuthorName(anyString()))
//                .thenReturn(Optional.of(new Author("Author Name")));
//    }
//
//
//    @Test
//    void testGetBookDetail_Success() {
//        Long bookId = 1L;
//        Publisher publisher = new Publisher();
//        publisher.setPublisherId(1L);
//        publisher.setPublisherName("Test Publisher");
//
//        Book book = new Book();
//        book.setBookId(bookId);
//        book.setBookTitle("Test Book");
//        book.setPublisher(publisher);
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//
//        BookDetailResponseDto result = bookService.getBookDetail(bookId);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getBookTitle()).isEqualTo("Test Book");
//        verify(bookRepository, times(1)).findById(bookId);
//    }
//
//    @Test
//    void testGetBookDetail_BookNotFound() {
//        // Given
//        Long bookId = 1L;
//        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(BookNotFoundException.class, () -> bookService.getBookDetail(bookId));
//        verify(bookRepository, times(1)).findById(bookId);
//    }
//
//    @Test
//    void testRegisterBook_Success() {
//        BookRegisterRequestDto requestDto = new BookRegisterRequestDto(
//                1L, "Test Book", "Index", "Description",
//                LocalDate.of(2023, 1, 1), BigDecimal.valueOf(15000),
//                "1234567890123", "Test Publisher",
//                "https://example.com/image.jpg",
//                List.of(new CategoryResponseDto(1L, "Fiction", 1, null, null)),
//                List.of("Author1", "Author2")
//        );
//
//        bookService.registerBook(requestDto);
//
//        verify(bookRepository).save(any(Book.class));
//    }
//
//
//    @Test
//    void testDeleteBook_Success() {
//        Long bookId = 1L;
//        when(bookRepository.existsById(bookId)).thenReturn(true);
//
//        bookService.deleteBook(bookId);
//
//        verify(bookRepository, times(1)).deleteById(bookId);
//    }
//
//
//    @Test
//    void testDeleteBook_BookNotFound() {
//        // Given
//        Long bookId = 1L;
//        when(bookRepository.existsById(bookId)).thenReturn(false);
//
//        // When & Then
//        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
//        verify(bookRepository, times(1)).existsById(bookId);
//    }
//
//    @Test
//    void testGetBooks_Success() {
//        // Given
//        Pageable pageable = PageRequest.of(0, 10);
//        List<Book> books = List.of(new Book(), new Book());
//        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
//
//        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
//
//        // When
//        Page<BookRegisterDto> result = bookService.getBooks(pageable);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getTotalElements()).isEqualTo(2);
//
//        verify(bookRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    void testUpdateBook_Success() {
//        // Given
//        BookRegisterRequestDto requestDto = new BookRegisterRequestDto(
//                1L,
//                "Updated Title",
//                "Updated Index",
//                "Updated Description",
//                LocalDate.of(2023, 1, 1),
//                BigDecimal.valueOf(15000),
//                "1234567890123",
//                "Updated Publisher",
//                "https://example.com/updated-image.jpg",
//                List.of(new CategoryResponseDto(1L, "국내도서", 1, null, null)),
//                List.of("Author1", "Author2")
//        );
//
//        Book book = new Book();
//        book.setBookId(1L);
//
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
//        when(publisherRepository.findByPublisherName(anyString())).thenReturn(Optional.of(new Publisher()));
//
//        // When
//        bookService.updateBook(requestDto);
//
//        // Then
//        verify(bookRepository, times(1)).findById(1L);
//        verify(bookRepository, times(1)).save(any(Book.class));
//    }
//}
