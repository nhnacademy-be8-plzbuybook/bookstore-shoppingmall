package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellinBookResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SellingBookControllerTest {

    @InjectMocks
    private SellingBookController sellingBookController;

    @Mock
    private SellingBookService sellingBookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBooks() {
        // Mock 반환값 설정
        Page<SellingBookAndBookResponseDto> mockPage = new PageImpl<>(
                List.of(new SellingBookAndBookResponseDto(1L, 1L, "Book Title", BigDecimal.valueOf(10000),
                        true, 10, SellingBook.SellingBookStatus.SELLING, false, 100L,
                        "imageUrl", "publisherName", List.of("Category1"), List.of("Author1")))
        );
        Mockito.when(sellingBookService.getBooks(any(Pageable.class), anyString())).thenReturn(mockPage);

        // API 호출
        ResponseEntity<Page<SellingBookAndBookResponseDto>> response = sellingBookController.getBooks(0, 16, "sellingBookId", "desc");

        // 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Book Title", response.getBody().getContent().get(0).getBookTitle());
    }


    @Test
    void registerSellingBooks() {
        SellingBookRegisterDto dto = new SellingBookRegisterDto();

        doNothing().when(sellingBookService).registerSellingBooks(any(SellingBookRegisterDto.class));

        ResponseEntity<SellingBookRegisterDto> response = sellingBookController.registerSellingBooks(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());

        verify(sellingBookService, times(1)).registerSellingBooks(eq(dto));
    }

    @Test
    void deleteSellingBook() {
        Long sellingBookId = 1L;

        doNothing().when(sellingBookService).deleteSellingBook(eq(sellingBookId));

        ResponseEntity<Void> response = sellingBookController.deleteSellingBook(sellingBookId);

        assertEquals(204, response.getStatusCodeValue());

        verify(sellingBookService, times(1)).deleteSellingBook(eq(sellingBookId));
    }

    @Test
    void updateSellingBook() {
        Long sellingBookId = 1L;
        SellingBookRegisterDto dto = new SellingBookRegisterDto();
        SellinBookResponseDto responseDto = new SellinBookResponseDto();

        when(sellingBookService.updateSellingBook(eq(sellingBookId), eq(dto))).thenReturn(responseDto);

        ResponseEntity<SellinBookResponseDto> response = sellingBookController.updateSellingBook(sellingBookId, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());

        verify(sellingBookService, times(1)).updateSellingBook(eq(sellingBookId), eq(dto));
    }

    @Test
    void getSellingBook() {
        Long sellingBookId = 1L;
        BookDetailResponseDto responseDto = new BookDetailResponseDto();

        when(sellingBookService.getSellingBook(eq(sellingBookId))).thenReturn(responseDto);

        ResponseEntity<BookDetailResponseDto> response = sellingBookController.getSellingBook(sellingBookId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());

        verify(sellingBookService, times(1)).getSellingBook(eq(sellingBookId));
    }

    @Test
    void getSellingBooksByViewCount() {
        List<SellingBookAndBookResponseDto> responseDtos = Collections.emptyList();

        when(sellingBookService.getSellingBooksByViewCount(eq("desc"))).thenReturn(responseDtos);

        ResponseEntity<List<SellingBookAndBookResponseDto>> response = sellingBookController.getSellingBooksByViewCount("desc");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDtos, response.getBody());

        verify(sellingBookService, times(1)).getSellingBooksByViewCount(eq("desc"));
    }

    @Test
    void getSellingBooksByStatus() {
        SellingBook.SellingBookStatus status = SellingBook.SellingBookStatus.SELLING;
        List<SellingBookAndBookResponseDto> responseDtos = Collections.emptyList();

        when(sellingBookService.getSellingBooksByStatus(eq(status))).thenReturn(responseDtos);

        ResponseEntity<List<SellingBookAndBookResponseDto>> response = sellingBookController.getSellingBooksByStatus(status);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDtos, response.getBody());

        verify(sellingBookService, times(1)).getSellingBooksByStatus(eq(status));
    }

    @Test
    void getSellingBooksByViewCountDesc() {
        List<SellingBookAndBookResponseDto> responseDtos = Collections.emptyList();

        when(sellingBookService.getSellingBooksByViewCount(eq("desc"))).thenReturn(responseDtos);

        ResponseEntity<List<SellingBookAndBookResponseDto>> response = sellingBookController.getSellingBooksByViewCountDesc();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDtos, response.getBody());

        verify(sellingBookService, times(1)).getSellingBooksByViewCount(eq("desc"));
    }

    @Test
    void getSellingBooksByViewCountAsc() {
        List<SellingBookAndBookResponseDto> responseDtos = Collections.emptyList();

        when(sellingBookService.getSellingBooksByViewCount(eq("asc"))).thenReturn(responseDtos);

        ResponseEntity<List<SellingBookAndBookResponseDto>> response = sellingBookController.getSellingBooksByViewCountAsc();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDtos, response.getBody());

        verify(sellingBookService, times(1)).getSellingBooksByViewCount(eq("asc"));
    }

    @Test
    void getSellingBooksByCategory() {
        Long categoryId = 1L;
        List<SellingBookAndBookResponseDto> responseDtos = Collections.emptyList();

        when(sellingBookService.getSellingBooksByCategory(eq(categoryId))).thenReturn(responseDtos);

        ResponseEntity<List<SellingBookAndBookResponseDto>> response = sellingBookController.getSellingBooksByCategory(categoryId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDtos, response.getBody());

        verify(sellingBookService, times(1)).getSellingBooksByCategory(eq(categoryId));
    }
}
