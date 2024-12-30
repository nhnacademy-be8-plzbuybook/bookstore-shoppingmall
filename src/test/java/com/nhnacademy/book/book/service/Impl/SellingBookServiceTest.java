package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.BookImageRepository;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellingBookServiceTest {

    @InjectMocks
    private SellingBookService sellingBookService;

    @Mock
    private SellingBookRepository sellingBookRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookImageRepository bookImageRepository;

    @Mock
    private SellingBook sellingBook;

    @Mock
    private Book book;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setBookId(1L);

        sellingBook = new SellingBook();
        sellingBook.setSellingBookId(1L);
        sellingBook.setBook(book);
        sellingBook.setSellingBookPrice(new BigDecimal("19.99"));
        sellingBook.setSellingBookPackageable(true);
        sellingBook.setSellingBookStock(100);
        sellingBook.setSellingBookStatus(SellingBookStatus.SELLING);
        sellingBook.setUsed(false);
        sellingBook.setSellingBookViewCount(50L);
    }

    @Test
    void getBooks() {
        // Mock 데이터 생성
        List<SellingBook> sellingBooks = new ArrayList<>();
        sellingBooks.add(sellingBook);

        BookImage bookImage = new BookImage();
        bookImage.setImageUrl("http://example.com/image.jpg");

        // Mock 메서드 동작 정의
        when(sellingBookRepository.findAll()).thenReturn(sellingBooks);
        when(bookImageRepository.findByBook(any(Book.class))).thenReturn(Optional.of(bookImage));

        // 메서드 실행
        List<SellingBookResponseDto> responseDtos = sellingBookService.getBooks();

        // 검증
        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals("http://example.com/image.jpg", responseDtos.get(0).getImageUrl());

        // Mock 메서드 호출 검증
        verify(sellingBookRepository, times(1)).findAll();
        verify(bookImageRepository, times(1)).findByBook(any(Book.class));
    }

    @Test
    void getSellingBooksByViewCount() {
        // Mock 데이터 생성
        List<SellingBook> sellingBooks = new ArrayList<>();
        sellingBooks.add(sellingBook);

        // SellingBookRepository의 findAll(Sort) 호출에 대한 동작 정의
        when(sellingBookRepository.findAll(any(Sort.class))).thenReturn(sellingBooks);

        // Service의 getSellingBooksByViewCount() 메서드 호출 (내림차순 정렬)
        List<SellingBookResponseDto> responseDtos = sellingBookService.getSellingBooksByViewCount("desc");

        // 반환값 검증
        assertNotNull(responseDtos); // 반환된 리스트가 null이 아님을 검증
        assertEquals(1, responseDtos.size()); // 반환된 리스트의 크기 검증
        assertEquals(50L, responseDtos.get(0).getSellingBookViewCount()); // 조회수 검증

        // Mock 메서드 호출 검증
        verify(sellingBookRepository, times(1)).findAll(any(Sort.class)); // findAll()이 Sort 조건과 함께 호출되었는지 확인
    }

    @Test
    void getSellingBooksByStatus() {
        // Mock 데이터 생성
        List<SellingBook> sellingBooks = new ArrayList<>();
        sellingBooks.add(sellingBook);

        // SellingBookRepository의 findBySellingBookStatus() 호출에 대한 동작 정의
        when(sellingBookRepository.findBySellingBookStatus(SellingBookStatus.SELLING)).thenReturn(sellingBooks);

        // Service의 getSellingBooksByStatus() 메서드 호출
        List<SellingBookResponseDto> responseDtos = sellingBookService.getSellingBooksByStatus(SellingBookStatus.SELLING);

        // 반환값 검증
        assertNotNull(responseDtos); // 반환된 리스트가 null이 아님을 검증
        assertEquals(1, responseDtos.size()); // 반환된 리스트의 크기 검증
        assertEquals(SellingBookStatus.SELLING, responseDtos.get(0).getSellingBookStatus()); // 상태 검증

        // Mock 메서드 호출 검증
        verify(sellingBookRepository, times(1)).findBySellingBookStatus(SellingBookStatus.SELLING); // 특정 상태로 검색이 호출되었는지 확인
    }

    @Test
    void getSellingBooksByPriceRange() {
        // Mock 데이터 생성
        List<SellingBook> sellingBooks = new ArrayList<>();
        sellingBooks.add(sellingBook);

        // SellingBookRepository의 findBySellingBookPriceBetween() 호출에 대한 동작 정의
        when(sellingBookRepository.findBySellingBookPriceBetween(new BigDecimal("10.00"), new BigDecimal("30.00"))).thenReturn(sellingBooks);

        // Service의 getSellingBooksByPriceRange() 메서드 호출
        List<SellingBookResponseDto> responseDtos = sellingBookService.getSellingBooksByPriceRange(new BigDecimal("10.00"), new BigDecimal("30.00"));

        // 반환값 검증
        assertNotNull(responseDtos); // 반환된 리스트가 null이 아님을 검증
        assertEquals(1, responseDtos.size()); // 반환된 리스트의 크기 검증
        assertTrue(responseDtos.get(0).getSellingBookPrice().compareTo(new BigDecimal("10.00")) >= 0); // 가격 범위 하한 검증
        assertTrue(responseDtos.get(0).getSellingBookPrice().compareTo(new BigDecimal("30.00")) <= 0); // 가격 범위 상한 검증

        // Mock 메서드 호출 검증
        verify(sellingBookRepository, times(1)).findBySellingBookPriceBetween(new BigDecimal("10.00"), new BigDecimal("30.00"));
    }

    @Test
    void calculateOrderPrice() {
        // SellingBookRepository의 findById() 호출에 대한 동작 정의
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));

        // Service의 calculateOrderPrice() 메서드 호출
        BigDecimal totalPrice = sellingBookService.calculateOrderPrice(1L, 5);

        // 반환값 검증
        assertNotNull(totalPrice); // 총 금액이 null이 아님을 검증
        assertEquals(new BigDecimal("99.95"), totalPrice); // 총 금액 계산값 검증 (19.99 × 5)

        // Mock 메서드 호출 검증
        verify(sellingBookRepository, times(1)).findById(1L); // findById()가 호출되었는지 확인
    }

    @Test
    void calculateOrderPrice_InsufficientStock() {
        // 재고를 부족하게 설정
        sellingBook.setSellingBookStock(3);

        // SellingBookRepository의 findById() 호출에 대한 동작 정의
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));

        // 재고 부족 예외 검증
        assertThrows(IllegalArgumentException.class, () -> sellingBookService.calculateOrderPrice(1L, 5));

        // Mock 메서드 호출 검증
        verify(sellingBookRepository, times(1)).findById(1L); // findById()가 호출되었는지 확인
    }

}
