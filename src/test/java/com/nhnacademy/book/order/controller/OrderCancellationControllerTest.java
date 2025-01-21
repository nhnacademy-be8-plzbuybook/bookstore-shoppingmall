package com.nhnacademy.book.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.service.OrderCancellationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderCancellationController.class)
class OrderCancellationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderCancellationService orderCancellationService;
    private static final String BASE_URL = "/api/orders";
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cancelOrderProduct() throws Exception {
        String orderId = "orderId";
        String path = "/" + orderId + "/cancel";

        OrderCancelRequestDto cancelRequest = new OrderCancelRequestDto();
        doNothing().when(orderCancellationService).cancelOrderProducts(anyString(), any(OrderCancelRequestDto.class));

        // ArgumentCaptor 생성
        ArgumentCaptor<OrderCancelRequestDto> dtoCaptor = ArgumentCaptor.forClass(OrderCancelRequestDto.class);

        mockMvc.perform(post(BASE_URL + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelRequest)))
                .andExpect(status().isNoContent());

        // verify with captor
        verify(orderCancellationService).cancelOrderProducts(eq(orderId), dtoCaptor.capture());

        // 필요한 필드들만 검증
        OrderCancelRequestDto capturedDto = dtoCaptor.getValue();
        assertEquals(capturedDto, cancelRequest);
    }
}