package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private SellingBook sellingBook;
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
    void registerSellingBook() {
        SellingBookRegisterDto registerDto = new SellingBookRegisterDto();
        registerDto.setBookId(1L);
        registerDto.setPrice(new BigDecimal("19.99"));
        registerDto.setPackageable(true);
        registerDto.setStock(100);
        registerDto.setUsed(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(sellingBookRepository.save(any(SellingBook.class))).thenReturn(sellingBook);

        SellingBookResponseDto responseDto = sellingBookService.registerSellingBook(registerDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getSellingBookId());
        assertEquals(new BigDecimal("19.99"), responseDto.getSellingBookPrice());
        verify(bookRepository, times(1)).findById(1L);
        verify(sellingBookRepository, times(1)).save(any(SellingBook.class));
    }

    @Test
    void registerSellingBook_BookNotFound() {
        SellingBookRegisterDto registerDto = new SellingBookRegisterDto();
        registerDto.setBookId(999L);

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> sellingBookService.registerSellingBook(registerDto));
        verify(bookRepository, times(1)).findById(999L);
    }

    @Test
    void getSellingBook() {
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));

        SellingBookResponseDto responseDto = sellingBookService.getSellingBook(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getSellingBookId());
        verify(sellingBookRepository, times(1)).findById(1L);
    }

    @Test
    void getSellingBook_NotFound() {
        when(sellingBookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(SellingBookNotFoundException.class, () -> sellingBookService.getSellingBook(999L));
        verify(sellingBookRepository, times(1)).findById(999L);
    }

    @Test
    void updateSellingBook() {
        SellingBookRegisterDto updateDto = new SellingBookRegisterDto();
        updateDto.setPrice(new BigDecimal("29.99"));
        updateDto.setPackageable(false);
        updateDto.setStock(200);
        updateDto.setUsed(true);

        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));
        when(sellingBookRepository.save(any(SellingBook.class))).thenReturn(sellingBook);

        SellingBookResponseDto responseDto = sellingBookService.updateSellingBook(1L, updateDto);

        assertNotNull(responseDto);
        assertEquals(new BigDecimal("29.99"), responseDto.getSellingBookPrice());
        assertEquals(200, responseDto.getSellingBookStock());
        assertTrue(responseDto.getUsed());
        verify(sellingBookRepository, times(1)).findById(1L);
        verify(sellingBookRepository, times(1)).save(any(SellingBook.class));
    }

    @Test
    void deleteSellingBook() {
        when(sellingBookRepository.existsById(1L)).thenReturn(true);

        sellingBookService.deleteSellingBook(1L);

        verify(sellingBookRepository, times(1)).existsById(1L);
        verify(sellingBookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSellingBook_NotFound() {
        when(sellingBookRepository.existsById(999L)).thenReturn(false);

        assertThrows(SellingBookNotFoundException.class, () -> sellingBookService.deleteSellingBook(999L));
        verify(sellingBookRepository, times(1)).existsById(999L);
    }

}
