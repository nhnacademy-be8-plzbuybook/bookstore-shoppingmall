package com.nhnacademy.book.cartbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.service.CartBookGuestService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CartBookGuestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartBookGuestService cartBookGuestService;

    @Spy
    @InjectMocks
    private CartBookGuestController cartBookGuestController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartBookGuestController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("비회원 장바구니에 책 추가 성공 테스트")
    void createGuestCart_Success() throws Exception {
        // Given
        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(1L, 2);
        Long expectedCartId = 1L;

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookGuestService.AddToGuestCart(any(CreateCartBookRequestDto.class), anyString()))
                .thenReturn(expectedCartId);

        // When & Then
        mockMvc.perform(post("/api/bookstore/guests/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("cart", "test-session-id"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(expectedCartId)); // 응답 확인
    }

    @Test
    @DisplayName("비회원 장바구니 조회 성공 테스트")
    void getGuestCart_Success() throws Exception {
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
        when(cartBookGuestService.getGuestCart(anyString())).thenReturn(cartBooks);

        // When & Then
        mockMvc.perform(get("/api/bookstore/guests/carts")
                        .header("cart", "test-session-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cartId").value(1L)) // 응답 확인
                .andExpect(jsonPath("$[0].bookTitle").value("Test Book")); // 응답 확인
    }

    @Test
    @DisplayName("비회원 장바구니 아이템 수정 성공 테스트")
    void updateGuestCartItem_Success() throws Exception {
        // Given
        Long expectedCartId = 1L;
        Long cartId = 1L;
        int quantity = 2;

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookGuestService.updateGuestCartItem(cartId, quantity, "test-session-id"))
                .thenReturn(expectedCartId);

        // When & Then
        mockMvc.perform(put("/api/bookstore/guests/carts")
                        .param("cartId", String.valueOf(cartId))
                        .param("quantity", String.valueOf(quantity))
                        .header("cart", "test-session-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(expectedCartId)); // 응답 확인
    }

    @Test
    @DisplayName("비회원 장바구니 비우기 성공 테스트")
    void clearGuestCart_Success() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/bookstore/guests/carts")
                        .header("cart", "test-session-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent()); // 응답 확인
    }

    @Test
    @DisplayName("비회원 장바구니에서 아이템 삭제 성공 테스트")
    void removeItemFromGuestCart_Success() throws Exception {
        // Given
        Long cartId = 1L;
        Long expectedCartId = 1L;

        // 모킹: 서비스에서 반환할 값 설정
        when(cartBookGuestService.removeItemFromGuestCart(cartId, "test-session-id"))
                .thenReturn(expectedCartId);

        // When & Then
        mockMvc.perform(delete("/api/bookstore/guests/carts/{cartId}", cartId)
                        .header("cart", "test-session-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(expectedCartId)); // 응답 확인
    }
}
