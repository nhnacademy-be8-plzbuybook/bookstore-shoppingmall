package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellinBookResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.elastic.repository.BookInfoRepository;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellingBookServiceTest {

    @Mock
    private SellingBookRepository sellingBookRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookImageRepository bookImageRepository;

    @Mock
    private BookAuthorRepository bookAuthorRepository;

    @InjectMocks
    private SellingBookService sellingBookService;

    @Mock
    private BookInfoRepository bookInfoRepository;

    @Mock
    private LikesRepository likesRepository;
    private SellingBook testSellingBook;
    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트 데이터 초기화
        Publisher publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");

        testBook = new Book();
        testBook.setBookId(1L);
        testBook.setBookTitle("Test Book");
        testBook.setPublisher(publisher);

        testSellingBook = new SellingBook();
        testSellingBook.setSellingBookId(1L);
        testSellingBook.setBook(testBook);
        testSellingBook.setSellingBookPrice(new BigDecimal("29.99"));
        testSellingBook.setSellingBookStock(100);
        testSellingBook.setSellingBookStatus(SellingBookStatus.SELLING);
        testSellingBook.setUsed(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(testSellingBook));
    }

    @Test
    @DisplayName("판매책 등록 성공")
    void testRegisterSellingBooks_Success() {
        SellingBookRegisterDto registerDto = new SellingBookRegisterDto();
        registerDto.setBookId(1L);
        registerDto.setSellingBookPrice(new BigDecimal("19.99"));
        registerDto.setSellingBookPackageable(true);
        registerDto.setSellingBookStock(10);
        registerDto.setSellingBookStatus(SellingBookStatus.SELLING);
        registerDto.setSellingBookViewCount(100L);
        registerDto.setUsed(false);

        sellingBookService.registerSellingBooks(registerDto);

        verify(bookRepository, times(1)).findById(1L);
        verify(sellingBookRepository, times(1)).save(any(SellingBook.class));
    }

    @Test
    @DisplayName("존재하지 않는 책으로 판매책 등록 시 실패")
    void testRegisterSellingBooks_BookNotFound() {
        SellingBookRegisterDto registerDto = new SellingBookRegisterDto();
        registerDto.setBookId(99L); // 존재하지 않는 책 ID

        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> sellingBookService.registerSellingBooks(registerDto));

        verify(bookRepository, times(1)).findById(99L);
        verifyNoInteractions(sellingBookRepository);
    }

    @Test
    @DisplayName("판매책 수정 성공")
    void testUpdateSellingBook_Success() {
        SellingBookRegisterDto updateDto = new SellingBookRegisterDto();
        updateDto.setSellingBookPrice(new BigDecimal("39.99"));
        updateDto.setSellingBookStock(50);

        // save 시 반환값 설정
        when(sellingBookRepository.save(any(SellingBook.class))).thenReturn(testSellingBook);

        SellinBookResponseDto updatedSellingBook = sellingBookService.updateSellingBook(1L, updateDto);

        assertEquals(new BigDecimal("39.99"), updatedSellingBook.getSellingBookPrice());
        assertEquals(50, updatedSellingBook.getSellingBookStock());
        verify(sellingBookRepository, times(1)).findById(1L);
        verify(sellingBookRepository, times(1)).save(any(SellingBook.class));
    }


    @Test
    @DisplayName("존재하지 않는 판매책 수정 시 실패")
    void testUpdateSellingBook_NotFound() {
        SellingBookRegisterDto updateDto = new SellingBookRegisterDto();
        when(sellingBookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SellingBookNotFoundException.class,
                () -> sellingBookService.updateSellingBook(99L, updateDto));

        verify(sellingBookRepository, times(1)).findById(99L);
        verify(sellingBookRepository, never()).save(any(SellingBook.class));
    }

    @Test
    @DisplayName("판매책 삭제 성공 테스트")
    void testDeleteSellingBook_Success() {
        // Mock 설정
        when(sellingBookRepository.existsById(1L)).thenReturn(true);

        // 메서드 호출
        sellingBookService.deleteSellingBook(1L);

        // 검증
        verify(sellingBookRepository, times(1)).deleteById(1L);
        verify(bookInfoRepository, times(1)).deleteBySellingBookId(1L);
    }
    @Test
    @DisplayName("존재하지 않는 판매책 삭제 시 실패")
    void testDeleteSellingBook_NotFound() {
        when(sellingBookRepository.existsById(99L)).thenReturn(false);

        assertThrows(SellingBookNotFoundException.class,
                () -> sellingBookService.deleteSellingBook(99L));

        verify(sellingBookRepository, times(1)).existsById(99L);
        verify(sellingBookRepository, never()).deleteById(99L);
    }

    @Test
    @DisplayName("판매책 상세 조회 성공")
    void testGetSellingBook_Success() {
        // Given
        BookImage bookImage = new BookImage();
        bookImage.setImageUrl("test-image-url");

        List<String> categoryNames = List.of("Fiction", "Mystery");
        List<String> authorNames = List.of("John Doe", "Jane Smith");
        Long likeCount = 15L;

        // Mock 설정
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(testSellingBook)); // 판매책 조회
        when(bookImageRepository.findByBook(testBook)).thenReturn(Optional.of(bookImage)); // 책 이미지 조회
        when(categoryRepository.findCategoriesByBookId(1L))
                .thenReturn(categoryNames.stream().map(name -> {
                    Category category = new Category();
                    category.setCategoryName(name);
                    return category;
                }).collect(Collectors.toList())); // 카테고리 조회
        when(bookAuthorRepository.findByBook_BookId(1L))
                .thenReturn(authorNames.stream().map(name -> {
                    BookAuthor bookAuthor = new BookAuthor();
                    Author author = new Author();
                    author.setAuthorName(name);
                    bookAuthor.setAuthor(author);
                    return bookAuthor;
                }).collect(Collectors.toList())); // 작가 조회
        when(likesRepository.countBySellingBookId(1L)).thenReturn(likeCount); // 좋아요 수 조회

        // When
        BookDetailResponseDto response = sellingBookService.getSellingBook(1L);

        // Then
        assertNotNull(response);
        assertEquals("Test Book", response.getBookTitle());
        assertEquals("test-image-url", response.getImageUrl());
        assertEquals(categoryNames, response.getCategories());
        assertEquals(authorNames, response.getAuthorName()xww);
        assertEquals(likeCount, response.getLikeCount());

        // Verify
        verify(sellingBookRepository, times(1)).findById(1L);
        verify(bookImageRepository, times(1)).findByBook(testBook);
        verify(categoryRepository, times(1)).findCategoriesByBookId(1L);
        verify(bookAuthorRepository, times(1)).findByBook_BookId(1L);
        verify(likesRepository, times(1)).countBySellingBookId(1L);
    }


    @Test
    @DisplayName("존재하지 않는 판매책 상세 조회 시 실패")
    void testGetSellingBook_NotFound() {
        when(sellingBookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SellingBookNotFoundException.class,
                () -> sellingBookService.getSellingBook(99L));

        verify(sellingBookRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("판매책 조회수 기준 정렬 조회")
    void testGetSellingBooksByViewCount() {
        when(sellingBookRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(testSellingBook));

        List<SellingBookAndBookResponseDto> result = sellingBookService.getSellingBooksByViewCount("desc");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellingBookRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    @DisplayName("판매책 상태별 조회")
    void testGetSellingBooksByStatus() {
        when(sellingBookRepository.findBySellingBookStatus(SellingBookStatus.SELLING)).thenReturn(Collections.singletonList(testSellingBook));

        List<SellingBookAndBookResponseDto> result = sellingBookService.getSellingBooksByStatus(SellingBookStatus.SELLING);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellingBookRepository, times(1)).findBySellingBookStatus(SellingBookStatus.SELLING);
    }

    @Test
    @DisplayName("판매책 가격 범위별 조회")
    void testGetSellingBooksByPriceRange() {
        when(sellingBookRepository.findBySellingBookPriceBetween(new BigDecimal("10.00"), new BigDecimal("50.00")))
                .thenReturn(Collections.singletonList(testSellingBook));

        List<SellingBookAndBookResponseDto> result = sellingBookService.getSellingBooksByPriceRange(new BigDecimal("10.00"), new BigDecimal("50.00"));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellingBookRepository, times(1)).findBySellingBookPriceBetween(new BigDecimal("10.00"), new BigDecimal("50.00"));
    }
}
