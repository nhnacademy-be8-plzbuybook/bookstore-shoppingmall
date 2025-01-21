package com.nhnacademy.book.cartbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.request.DeleteCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.request.UpdateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.service.CartBookMemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CartBookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartBookMemberService cartBookMemberService;

    @Spy
    @InjectMocks
    private CartBookController cartBookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartBookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("장바구니 책 목록 조회 성공 테스트")
    void getAllCartBooks_Success() throws Exception {
        // Given
        ReadCartBookResponseDto dto = new ReadCartBookResponseDto(
                1L, // cartId
                1L, // cartBookId
                1L, // sellingBookId
                "Test Book", // bookTitle
                BigDecimal.valueOf(10000), // sellingBookPrice
                "http://example.com/image.jpg", // imageUrl
                1, // quantity
                100 // sellingBookStock
        );
        List<ReadCartBookResponseDto> cartBooks = Collections.singletonList(dto);

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookMemberService.readAllCartMember(any(String.class))).thenReturn(cartBooks);

        // When & Then
        mockMvc.perform(get("/api/bookstore/carts")
                        .header("X-USER-ID", "test@example.com")
                        .accept(MediaType.APPLICATION_JSON)) // JSON 응답을 기대
                .andExpect(status().isOk()) // 상태 코드가 200인지 확인
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON 응답을 기대
                .andExpect(jsonPath("$").isArray()) // 응답이 배열인지 확인
                .andExpect(jsonPath("$[0].cartId").value(1L)) // 첫 번째 요소의 cartId 확인
                .andExpect(jsonPath("$[0].bookTitle").value("Test Book")); // 첫 번째 요소의 bookTitle 확인

    }

    @Test
    @DisplayName("장바구니 책 추가 성공 테스트")
    void createBookCart_Success() throws Exception {
        // Given
        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(1L, 2);
        Long expectedId = 1L;

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookMemberService.createBookCartMember(any(CreateCartBookRequestDto.class), any(String.class)))
                .thenReturn(expectedId);

        // When & Then
        mockMvc.perform(post("/api/bookstore/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-USER-ID", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedId));
    }

    @Test
    @DisplayName("장바구니 책 수정 성공 테스트")
    void updateBookCart_Success() throws Exception {
        // Given
        UpdateCartBookRequestDto requestDto = new UpdateCartBookRequestDto(1L, 1L, 2);
        Long expectedId = 1L;

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookMemberService.updateBookCartMember(any(UpdateCartBookRequestDto.class), any(String.class)))
                .thenReturn(expectedId);

        // When & Then
        mockMvc.perform(put("/api/bookstore/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-USER-ID", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedId));
    }

    @Test
    @DisplayName("장바구니 책 삭제 성공 테스트")
    void deleteBookCart_Success() throws Exception {
        // Given
        DeleteCartBookRequestDto requestDto = new DeleteCartBookRequestDto(1L, 1L);
        String expectedMessage = "장바구니에서 삭제되었습니다.";

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookMemberService.deleteBookCartMember(any(Long.class), any(String.class)))
                .thenReturn(expectedMessage);

        // When & Then
        mockMvc.perform(delete("/api/bookstore/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-USER-ID", "test@example.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedMessage));
    }

    @Test
    @DisplayName("장바구니 전체 비우기 성공 테스트")
    void deleteAllBooks_Success() throws Exception {
        // Given
        String expectedMessage = "장바구니가 비워졌습니다.";

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookMemberService.deleteAllBookCartMember(any(String.class)))
                .thenReturn(expectedMessage);

        // When & Then
        mockMvc.perform(delete("/api/bookstore/carts/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", "test@example.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedMessage));
    }
}
