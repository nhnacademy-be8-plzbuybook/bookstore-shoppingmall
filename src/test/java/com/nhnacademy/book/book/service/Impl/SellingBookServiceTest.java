package com.nhnacademy.book.book.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.controller.SellingBookController;
import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.entity.SellingBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellingBookController.class)
class SellingBookServiceTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc: 컨트롤러 테스트를 위한 HTTP 요청/응답 시뮬레이션 도구입니다.

    @MockBean
    private SellingBookService sellingBookService; // @MockBean: 서비스 계층을 Mock으로 생성하여 테스트에서 실제 구현 대신 사용합니다.

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper: JSON 데이터 직렬화/역직렬화에 사용됩니다.

    private SellingBookRegisterDto registerDto; // 판매 책 등록 DTO
    private SellingBookRegisterDto updateDto; // 판매 책 업데이트 DTO
    private BookDetailResponseDto bookDetailResponseDto; // 판매 책 상세 DTO
    private Page<SellingBookAndBookResponseDto> bookPage; // 페이징된 책 데이터

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        registerDto = new SellingBookRegisterDto();
        registerDto.setBookId(1L);
        registerDto.setSellingBookPrice(new BigDecimal("19.99"));
        registerDto.setSellingBookPackageable(true);
        registerDto.setSellingBookStock(100);
        registerDto.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        registerDto.setSellingBookViewCount(50L);
        registerDto.setUsed(false);

        updateDto = new SellingBookRegisterDto();
        updateDto.setBookId(1L);
        updateDto.setSellingBookPrice(new BigDecimal("29.99"));
        updateDto.setSellingBookPackageable(true);
        updateDto.setSellingBookStock(80);
        updateDto.setSellingBookStatus(SellingBook.SellingBookStatus.SELLEND);
        updateDto.setSellingBookViewCount(100L);
        updateDto.setUsed(true);

        bookDetailResponseDto = new BookDetailResponseDto(
                1L, 1L, "Book Title", "Index", "Description", null,
                new BigDecimal("29.99"), new BigDecimal("25.99"),
                50, "978-3-16-148410-0", 1L, "Publisher Name",
                "image_url", Collections.singletonList("Category1"),
                Collections.singletonList("Author1"), "Available", 100L
        );

        PageRequest pageable = PageRequest.of(0, 16);
        bookPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Test
    void testGetBooks() throws Exception {
        // 페이징 요청 객체 생성
        PageRequest pageable = PageRequest.of(0, 16, Sort.by(Sort.Direction.DESC, "sellingBookId"));
        when(sellingBookService.getBooks(pageable, "sellingBookId")).thenReturn(bookPage);

        // GET 요청 수행
        mockMvc.perform(MockMvcRequestBuilders.get("/api/selling-books")
                        .param("page", "0") // 페이지 번호
                        .param("size", "16") // 페이지 크기
                        .param("sortBy", "sellingBookId") // 정렬 기준
                        .param("sortDir", "desc")) // 정렬 방향
                .andExpect(status().isOk()); // 응답 상태 검증

        // 서비스 메서드 호출 여부 검증
        verify(sellingBookService, times(1)).getBooks(pageable, "sellingBookId");
    }

    @Test
    void testRegisterSellingBooks() throws Exception {
        // POST 요청으로 판매 책 등록 테스트
        mockMvc.perform(MockMvcRequestBuilders.post("/api/selling-books")
                        .contentType(MediaType.APPLICATION_JSON) // JSON 데이터 명시
                        .content(objectMapper.writeValueAsString(registerDto))) // DTO를 JSON으로 직렬화
                .andExpect(status().isCreated()); // 응답 상태 검증

        // 서비스 메서드 호출 검증
        verify(sellingBookService, times(1)).registerSellingBooks(registerDto);
    }

    @Test
    void testDeleteSellingBook() throws Exception {
        Long sellingBookId = 1L;

        // DELETE 요청으로 판매 책 삭제 테스트
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/selling-books/{sellingBookId}", sellingBookId))
                .andExpect(status().isNoContent()); // 응답 상태 검증

        // 서비스 메서드 호출 검증
        verify(sellingBookService, times(1)).deleteSellingBook(sellingBookId);
    }

    @Test
    void testUpdateSellingBook() throws Exception {
        Long sellingBookId = 1L;

        // PUT 요청으로 판매 책 업데이트 테스트
        mockMvc.perform(MockMvcRequestBuilders.put("/api/selling-books/{sellingBookId}", sellingBookId)
                        .contentType(MediaType.APPLICATION_JSON) // JSON 데이터 명시
                        .content(objectMapper.writeValueAsString(updateDto))) // DTO를 JSON으로 직렬화
                .andExpect(status().isOk()); // 응답 상태 검증

        // 서비스 메서드 호출 검증
        verify(sellingBookService, times(1)).updateSellingBook(sellingBookId, updateDto);
    }

    @Test
    void testGetSellingBook() throws Exception {
        Long sellingBookId = 1L;
        when(sellingBookService.getSellingBook(sellingBookId)).thenReturn(bookDetailResponseDto);

        // GET 요청으로 판매 책 상세 조회 테스트
        mockMvc.perform(MockMvcRequestBuilders.get("/api/selling-books/{sellingBookId}", sellingBookId))
                .andExpect(status().isOk()) // 응답 상태 검증
                .andExpect(jsonPath("$.bookId").value(1L)) // JSON 응답 검증
                .andExpect(jsonPath("$.bookTitle").value("Book Title")); // JSON 응답 검증
    }

    @Test
    void testGetSellingBooksByViewCount() throws Exception {
        List<SellingBookAndBookResponseDto> books = Collections.emptyList();
        when(sellingBookService.getSellingBooksByViewCount("desc")).thenReturn(books);

        // GET 요청으로 조회수 기준 판매 책 목록 조회 테스트
        mockMvc.perform(MockMvcRequestBuilders.get("/api/selling-books/view-count")
                        .param("sortDirection", "desc")) // 정렬 방향
                .andExpect(status().isOk()); // 응답 상태 검증

        // 서비스 메서드 호출 검증
        verify(sellingBookService, times(1)).getSellingBooksByViewCount("desc");
    }

}

