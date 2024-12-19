//package com.nhnacademy.book.book.service.mapping;
//
//import com.nhnacademy.book.book.dto.response.aladin.AladinResponse;
//import com.nhnacademy.book.book.entity.*;
//import com.nhnacademy.book.book.repository.*;
//import com.nhnacademy.book.book.service.category.ApiCategoryService;
//import com.nhnacademy.book.book.service.image.ImageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class MappingServiceTest {
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private AuthorRepository authorRepository;
//
//    @Mock
//    private PublisherRepository publisherRepository;
//
//    @Mock
//    private ImageService imageService;
//
//    @Mock
//    private ApiCategoryService categoryService;
//
//    @Mock
//    private SellingBookRepository sellingBookRepository;
//
//    @InjectMocks
//    private MappingService mappingService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @DisplayName("책 데이터 처리 성공 여부 확인")
//    @Test
//    void processBookData_Success() {
//        // Given
//        AladinResponse bookData = new AladinResponse();
//        bookData.setTitle("Test Title");
//        bookData.setIsbn13("1234567890123");
//        bookData.setPublisher("Test Publisher");
//        bookData.setAuthor("Author1,Author2");
//        bookData.setPriceStandard(10000);
//        bookData.setPubDate("2024-01-01");
//        bookData.setCover("test_cover.jpg");
//        bookData.setCategoryName("Category > Subcategory");
//
//        when(bookRepository.existsByBookIsbn13(bookData.getIsbn13())).thenReturn(false);
//        when(publisherRepository.findByPublisherName("Test Publisher")).thenReturn(Optional.empty());
//        when(publisherRepository.save(any(Publisher.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(authorRepository.findByAuthorName(anyString())).thenReturn(Optional.empty());
//        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(sellingBookRepository.save(any(SellingBook.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // When
//        boolean result = mappingService.processBookData(bookData);
//
//        // Then
//        assertTrue(result);
//        verify(bookRepository, times(1)).save(any(Book.class));
//        verify(imageService, times(1)).saveBookImage(any(Book.class), eq("test_cover.jpg"));
//        verify(categoryService, times(1)).processCategoryPath(eq("Category > Subcategory"));
//        verify(sellingBookRepository, times(1)).save(any(SellingBook.class));
//    }
//
//    @DisplayName("중복 데이터 처리 확인")
//    @Test
//    void processBookData_Duplicate() {
//        // Given
//        AladinResponse bookData = new AladinResponse();
//        bookData.setIsbn13("1234567890123");
//
//        when(bookRepository.existsByBookIsbn13(bookData.getIsbn13())).thenReturn(true);
//
//        // When
//        boolean result = mappingService.processBookData(bookData);
//
//        // Then
//        assertFalse(result);
//        verify(bookRepository, never()).save(any(Book.class));
//    }
//
//    @DisplayName("Book 엔티티 매핑 성공 여부 확인")
//    @Test
//    void mapToBookEntity_Success() {
//        // Given
//        AladinResponse response = new AladinResponse();
//        response.setTitle("Test Title");
//        response.setDescription("Test Description");
//        response.setIsbn13("1234567890123");
//        response.setPublisher("Test Publisher");
//        response.setAuthor("Author1,Author2");
//        response.setPriceStandard(20000);
//        response.setPubDate("2024-01-01");
//
//        when(publisherRepository.findByPublisherName("Test Publisher")).thenReturn(Optional.empty());
//        when(publisherRepository.save(any(Publisher.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(authorRepository.findByAuthorName(anyString())).thenReturn(Optional.empty());
//        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // When
//        Book book = mappingService.mapToBookEntity(response);
//
//        // Then
//        assertNotNull(book);
//        assertEquals("Test Title", book.getBookTitle());
//        assertEquals("1234567890123", book.getBookIsbn13());
//        assertEquals(LocalDate.of(2024, 1, 1), book.getBookPubDate());
//        verify(publisherRepository, times(1)).findByPublisherName("Test Publisher");
//        verify(authorRepository, times(2)).save(any(Author.class));
//    }
//
//    @DisplayName("Book 엔티티 매핑 중 날짜 파싱 실패 처리 확인")
//    @Test
//    void mapToBookEntity_InvalidDate() {
//        // Given
//        AladinResponse response = new AladinResponse();
//        response.setTitle("Test Title");
//        response.setDescription("Test Description");
//        response.setIsbn13("1234567890123");
//        response.setPublisher("Test Publisher");
//        response.setAuthor("Author1,Author2");
//        response.setPriceStandard(20000);
//        response.setPubDate("Invalid Date");
//
//        when(publisherRepository.findByPublisherName("Test Publisher")).thenReturn(Optional.empty());
//        when(publisherRepository.save(any(Publisher.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(authorRepository.findByAuthorName(anyString())).thenReturn(Optional.empty());
//        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // When
//        Book book = mappingService.mapToBookEntity(response);
//
//        // Then
//        assertNotNull(book);
//        assertNotNull(book.getBookPubDate()); // 현재 날짜가 설정되어야 함
//        verify(publisherRepository, times(1)).findByPublisherName("Test Publisher");
//        verify(authorRepository, times(2)).save(any(Author.class));
//    }
//}
