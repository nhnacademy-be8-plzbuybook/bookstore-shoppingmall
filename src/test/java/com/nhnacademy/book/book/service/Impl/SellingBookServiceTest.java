package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellinBookResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.elastic.repository.BookInfoRepository;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import com.nhnacademy.book.book.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private SellingBookService sellingBookService;

    private SellingBook testSellingBook;

    @Mock
    private BookInfoRepository bookInfoRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Publisher 초기화
        Publisher publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");

        // Book 초기화
        Book book = new Book();
        book.setBookId(1L);
        book.setBookTitle("Test Book");
        book.setPublisher(publisher);

        // SellingBook 초기화
        testSellingBook = new SellingBook();
        testSellingBook.setSellingBookId(1L);
        testSellingBook.setBook(book);
        testSellingBook.setSellingBookPrice(new BigDecimal("29.99"));
        testSellingBook.setSellingBookStock(100);
        testSellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        testSellingBook.setUsed(false);

        // BookImage Mock 설정
        BookImage bookImage = new BookImage();
        bookImage.setImageUrl("test-image-url");
        when(bookImageRepository.findByBook(any(Book.class))).thenReturn(Optional.of(bookImage));

//        // BookInfoRepository Mock 설정
//        when(bookInfoRepository.deleteBySellingBookId(anyLong())).thenReturn(null);
    }

    @Test
    @DisplayName("Get Books - Default Sort")
    void testGetBooks() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<SellingBook> page = new PageImpl<>(Collections.singletonList(testSellingBook));

        when(sellingBookRepository.findAll(pageable)).thenReturn(page);

        Page<SellingBookAndBookResponseDto> result = sellingBookService.getBooks(pageable, "default");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(sellingBookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Update Selling Book")
    void testUpdateSellingBook() {
        SellingBookRegisterDto dto = new SellingBookRegisterDto();
        dto.setSellingBookPrice(new BigDecimal("39.99"));
        dto.setSellingBookStock(50);

        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(testSellingBook));
        when(sellingBookRepository.save(any(SellingBook.class))).thenReturn(testSellingBook);

        SellinBookResponseDto response = sellingBookService.updateSellingBook(1L, dto);

        assertNotNull(response);
        assertEquals(new BigDecimal("39.99"), response.getSellingBookPrice());
        assertEquals(50, response.getSellingBookStock());
        verify(sellingBookRepository, times(1)).findById(1L);
        verify(sellingBookRepository, times(1)).save(any(SellingBook.class));
    }

//    @Test
//    @DisplayName("Register Selling Book")
//    void testRegisterSellingBooks() {
//        SellingBookRegisterDto dto = new SellingBookRegisterDto();
//        dto.setBookId(1L);
//        dto.setSellingBookPrice(new BigDecimal("19.99"));
//
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
//
//        sellingBookService.registerSellingBooks(dto);
//
//        verify(sellingBookRepository, times(1)).save(any(SellingBook.class));
//    }

    @Test
    @DisplayName("Delete Selling Book")
    void testDeleteSellingBook() {
        when(sellingBookRepository.existsById(1L)).thenReturn(true);

        sellingBookService.deleteSellingBook(1L);

        verify(sellingBookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Get Selling Book")
    void testGetSellingBook() {
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(testSellingBook));
        when(bookImageRepository.findByBook(testBook)).thenReturn(Optional.empty());

        BookDetailResponseDto response = sellingBookService.getSellingBook(1L);

        assertNotNull(response);
        assertEquals("Test Book", response.getBookTitle());
        verify(sellingBookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Selling Books by View Count")
    void testGetSellingBooksByViewCount() {
        when(sellingBookRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(testSellingBook));

        List<SellingBookAndBookResponseDto> result = sellingBookService.getSellingBooksByViewCount("desc");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellingBookRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    @DisplayName("Get Selling Books by Status")
    void testGetSellingBooksByStatus() {
        when(sellingBookRepository.findBySellingBookStatus(SellingBookStatus.SELLING)).thenReturn(Collections.singletonList(testSellingBook));

        List<SellingBookAndBookResponseDto> result = sellingBookService.getSellingBooksByStatus(SellingBookStatus.SELLING);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellingBookRepository, times(1)).findBySellingBookStatus(SellingBookStatus.SELLING);
    }

    @Test
    @DisplayName("Get Selling Books by Price Range")
    void testGetSellingBooksByPriceRange() {
        when(sellingBookRepository.findBySellingBookPriceBetween(new BigDecimal("10.00"), new BigDecimal("50.00")))
                .thenReturn(Collections.singletonList(testSellingBook));

        List<SellingBookAndBookResponseDto> result = sellingBookService.getSellingBooksByPriceRange(new BigDecimal("10.00"), new BigDecimal("50.00"));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellingBookRepository, times(1)).findBySellingBookPriceBetween(new BigDecimal("10.00"), new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Get Selling Books by Category")
    void testGetSellingBooksByCategory() {
        when(sellingBookRepository.findByCategoryIdOrParent(1L)).thenReturn(Collections.singletonList(testSellingBook));

        List<SellingBookAndBookResponseDto> result = sellingBookService.getSellingBooksByCategory(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sellingBookRepository, times(1)).findByCategoryIdOrParent(1L);
    }
}
