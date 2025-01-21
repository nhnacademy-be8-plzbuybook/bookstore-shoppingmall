package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.BookAuthorRequestDto;
import com.nhnacademy.book.book.dto.request.BookRegisterRequestDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.BookRegisterDto;
import com.nhnacademy.book.book.dto.response.CategoryResponseDto;
import com.nhnacademy.book.book.elastic.repository.BookInfoRepository;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    private EntityManager entityManager;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookInfoRepository bookInfoRepository;
    @Mock
    private BookImageRepository bookImageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookAuthorService bookAuthorService;


    @Mock
    private BookAuthorRepository bookAuthorRepository;
    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private BookCategoryService bookCategoryService;

    @BeforeEach
    @DisplayName("Mock 객체 초기화")
    void setUp() {
        // Mock 객체 초기화
        MockitoAnnotations.openMocks(this);

        // 공통 테스트 데이터 설정
        Publisher mockPublisher = new Publisher("Mock Publisher");
        Book mockBook = new Book();
        mockBook.setBookId(1L);
        mockBook.setBookTitle("Mock Book");
        mockBook.setPublisher(mockPublisher);

        // Mock 동작 설정
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsById(anyLong())).thenReturn(true);

        when(publisherRepository.findByPublisherName(anyString()))
                .thenReturn(Optional.of(mockPublisher));

        when(categoryRepository.findByCategoryId(anyLong()))
                .thenReturn(Optional.of(new Category("Mock Category")));

        when(authorRepository.findByAuthorName(anyString()))
                .thenReturn(Optional.of(new Author("Mock Author")));

        doNothing().when(entityManager).clear(); // EntityManager clear 호출 Mock
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
    @DisplayName("책 등록 실패 - 요청 DTO가 null인 경우")
    void shouldThrowException_WhenRegisterBookWithNullRequest() {
        assertThrows(BookNotFoundException.class, () -> bookService.registerBook(null));
    }

    @Test
    @DisplayName("책 등록 성공 - 카테고리 및 작가 포함")
    void shouldRegisterBookWithCategoriesAndAuthors() {
        BookRegisterRequestDto requestDto = new BookRegisterRequestDto(
                1L, "Test Book", "Index", "Description",
                LocalDate.of(2023, 1, 1), BigDecimal.valueOf(15000),
                "1234567890123", "Test Publisher",
                "http://example.com/image.jpg",
                List.of(new CategoryResponseDto(1L, "Fiction", 1, null, null)),
                List.of("Author1", "Author2")
        );

        bookService.registerBook(requestDto);

        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookImageRepository, times(1)).save(any(BookImage.class));
        verify(bookAuthorService, times(2)).createBookAuthor(any(BookAuthorRequestDto.class));
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
    @DisplayName("책 존재 여부 확인 - 존재할 경우")
    void shouldReturnTrue_WhenBookExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        boolean result = bookService.existsBook(1L);

        assertThat(result).isTrue();
        verify(bookRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("책 존재 여부 확인 - 존재하지 않을 경우")
    void shouldThrowException_WhenBookDoesNotExist() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.existsBook(1L));
        verify(bookRepository, times(1)).existsById(1L);
    }



    @Test
    @DisplayName("책 수정 값 가져오기 - 성공 (모든 필드 포함)")
    void shouldReturnBookUpdate_WhenBookExists() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookTitle("Test Book");
        book.setBookIndex("Index");
        book.setBookDescription("Description");
        book.setBookPubDate(LocalDate.of(2023, 1, 1));
        book.setBookPriceStandard(BigDecimal.valueOf(20000));
        book.setBookIsbn13("1234567890123");

        Publisher publisher = new Publisher("Test Publisher");
        book.setPublisher(publisher);

        // BookImages 설정
        BookImage bookImage = new BookImage(book, "http://example.com/image.jpg");
        book.setBookImages(List.of(bookImage));

        // BookAuthors 설정
        Author author = new Author("Author Name");
        BookAuthor bookAuthor = new BookAuthor(book, author);
        book.setBookAuthors(List.of(bookAuthor));

        // BookCategories 설정
        Category category = new Category("Fiction");
        BookCategory bookCategory = new BookCategory(book, category);
        book.setBookCategories(List.of(bookCategory));

        // Mock 설정
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        BookRegisterRequestDto result = bookService.getBookUpdate(bookId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBookTitle()).isEqualTo("Test Book");
        assertThat(result.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(result.getAuthors()).containsExactly("Author Name");
        assertThat(result.getCategories()).hasSize(1);
        assertThat(result.getCategories().get(0).getCategoryName()).isEqualTo("Fiction");
        verify(bookRepository, times(1)).findById(bookId);
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


    @Test
    @DisplayName("책 삭제 시 연관된 데이터도 삭제")
    void shouldDeleteRelatedData_WhenBookIsDeleted() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
        verify(bookInfoRepository, times(1)).deleteByBookId(bookId); // 메서드 이름 수정
    }


    @Test
    @DisplayName("책 목록 조회 - 빈 결과 반환")
    void shouldReturnEmptyPage_WhenNoBooksExist() {
        // Given
        Pageable pageable = mock(Pageable.class);
        when(bookRepository.findAll(pageable)).thenReturn(Page.empty());

        // When
        Page<BookRegisterDto> result = bookService.getBooks(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("책 목록 조회 - 여러 책 반환")
    void shouldReturnBooksPage_WhenBooksExist() {
        // Given
        Pageable pageable = mock(Pageable.class);

        // Mock 데이터 설정
        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setBookTitle("Book 1");
        book1.setPublisher(new Publisher("Publisher 1"));

        Book book2 = new Book();
        book2.setBookId(2L);
        book2.setBookTitle("Book 2");
        book2.setPublisher(new Publisher("Publisher 2"));

        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        // Mock 이미지, 카테고리, 작가 설정
        when(bookImageRepository.findByBook_BookId(anyLong()))
                .thenReturn(List.of(new BookImage(book1, "http://example.com/image1.jpg")));
        when(categoryRepository.findCategoriesByBookId(anyLong()))
                .thenReturn(List.of(new Category("Fiction")));
        when(bookAuthorRepository.findAuthorsByBookId(anyLong()))
                .thenReturn(List.of(new Author("Author 1")));

        // When
        Page<BookRegisterDto> result = bookService.getBooks(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        // 첫 번째 책 검증
        BookRegisterDto firstBook = result.getContent().get(0);
        assertThat(firstBook.getBookTitle()).isEqualTo("Book 1");
        assertThat(firstBook.getPublisher()).isEqualTo("Publisher 1");
        assertThat(firstBook.getImageUrl()).contains("http://example.com/image1.jpg");

        // 두 번째 책 검증
        BookRegisterDto secondBook = result.getContent().get(1);
        assertThat(secondBook.getBookTitle()).isEqualTo("Book 2");
        assertThat(secondBook.getPublisher()).isEqualTo("Publisher 2");

        // Repository 메서드 호출 검증
        verify(bookRepository, times(1)).findAll(pageable);
    }

}
