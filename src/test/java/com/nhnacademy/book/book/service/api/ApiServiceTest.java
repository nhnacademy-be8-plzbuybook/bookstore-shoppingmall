package com.nhnacademy.book.book.service.api;

import com.nhnacademy.book.book.dto.response.aladin.AladinBookListResponse;
import com.nhnacademy.book.book.dto.response.aladin.AladinResponse;
import com.nhnacademy.book.book.service.mapping.MappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MappingService mappingService;

    @InjectMocks
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("알라딘 상품 리스트 API로 데이터를 가져와 저장 성공 여부를 확인")
    @Test
    void saveBooksFromListApi() {
        // Given
        AladinBookListResponse mockResponse = new AladinBookListResponse();
        AladinResponse book = new AladinResponse();
        book.setTitle("Test Book");
        book.setIsbn13("1234567890123");
        mockResponse.setBooks(List.of(book));

        // API 호출에 대해 mockResponse 반환 설정
        when(restTemplate.getForObject(anyString(), eq(AladinBookListResponse.class))).thenReturn(mockResponse);
        when(mappingService.processBookData(book)).thenReturn(true);

        // When
        apiService.saveBooksFromListApi("Bestseller", "Book", 3, 1);

        // Then
        // 호출 횟수를 5로 변경
        verify(mappingService, times(5)).processBookData(book);
    }


    @DisplayName("ISBN 리스트를 기반으로 도서 데이터를 저장")
    @Test
    void saveBooksByIsbns() {
        // Given
        String isbn = "1234567890123";
        AladinBookListResponse mockResponse = new AladinBookListResponse();
        AladinResponse book = new AladinResponse();
        book.setIsbn13(isbn);
        mockResponse.setBooks(List.of(book));

        when(restTemplate.getForObject(anyString(), eq(AladinBookListResponse.class))).thenReturn(mockResponse);
        when(mappingService.processBookData(book)).thenReturn(true);

        // When
        boolean isSaved = apiService.saveBooksByIsbns(List.of(isbn));

        // Then
        assertTrue(isSaved);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(AladinBookListResponse.class));
        verify(mappingService, times(1)).processBookData(book);
    }

    @DisplayName("Item ID를 기반으로 도서 데이터를 저장")
    @Test
    void saveBooksByItemIds() {
        // Given
        String itemId = "12345";
        AladinBookListResponse mockResponse = new AladinBookListResponse();
        AladinResponse book = new AladinResponse();
        book.setIsbn13("1234567890123");
        mockResponse.setBooks(List.of(book));

        when(restTemplate.getForObject(anyString(), eq(AladinBookListResponse.class))).thenReturn(mockResponse);
        when(mappingService.processBookData(book)).thenReturn(true);

        // When
        apiService.saveBooksByItemIds(List.of(itemId));

        // Then
        verify(restTemplate, times(1)).getForObject(anyString(), eq(AladinBookListResponse.class));
        verify(mappingService, times(1)).processBookData(book);
    }

    @DisplayName("ISBN 리스트를 상세히 처리하며 실패한 ISBN을 확인")
    @Test
    void saveBooksByIsbnsDetailed() {
        // Given
        String isbn = "1234567890123";
        AladinBookListResponse mockResponse = new AladinBookListResponse();
        AladinResponse book = new AladinResponse();
        book.setIsbn13(isbn);
        mockResponse.setBooks(List.of(book));

        when(restTemplate.getForObject(anyString(), eq(AladinBookListResponse.class))).thenReturn(mockResponse);
        when(mappingService.processBookData(book)).thenReturn(true);

        // When
        List<String> failedIsbns = apiService.saveBooksByIsbnsDetailed(List.of(isbn));

        // Then
        assertTrue(failedIsbns.isEmpty());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(AladinBookListResponse.class));
        verify(mappingService, times(1)).processBookData(book);
    }
}