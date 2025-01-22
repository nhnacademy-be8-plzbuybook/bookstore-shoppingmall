package com.nhnacademy.book.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.service.OrderReturningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderReturnController.class)
class OrderReturnControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderReturningService orderReturningService;
    private static final String BASE_URL = "/api/orders";
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getOrderReturns() throws Exception {
        String path = "/order-product-returns";
        String trackingNumber = "tracking123";
        String status = "RETURNED";

        List<OrderProductReturnDto> orderReturns = List.of(mock(OrderProductReturnDto.class));
        Page<OrderProductReturnDto> orderProductReturnDtoPage = new PageImpl<>(orderReturns, PageRequest.of(0, 10), orderReturns.size());

        // When
        when(orderReturningService.getAllOrderProductReturns(any(OrderReturnSearchRequestDto.class), any(PageRequest.class)))
                .thenReturn(orderProductReturnDtoPage);

        // Then
        mockMvc.perform(get(BASE_URL + path)
                        .param("trackingNumber", trackingNumber)
                        .param("status", status)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void requestReturnOrderProduct() throws Exception {
        String orderId = "orderId";
        Long orderProductId = 1L;
        OrderProductReturnRequestDto returnRequest = new OrderProductReturnRequestDto("reason", 1, "123132123");
        String path = "/" + orderId + "/order-products/" + orderProductId + "/return";

        doNothing().when(orderReturningService).requestOrderProductReturn(eq(orderId), eq(orderProductId), any(OrderProductReturnRequestDto.class));
        mockMvc.perform(post(BASE_URL + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(returnRequest))
                )
                .andExpect(status().isOk());

        verify(orderReturningService).requestOrderProductReturn(eq(orderId), eq(orderProductId), any(OrderProductReturnRequestDto.class));
    }

    @Test
    void completeReturnOrderProduct() throws Exception {
        String orderId = "orderId";
        Long orderProductId = 1L;
        String path = "/" + orderId + "/order-products/" + orderProductId + "/return/complete";

        when(orderReturningService.completeOrderProductReturn(orderProductId)).thenReturn(orderProductId);
        MvcResult result = mockMvc.perform(post(BASE_URL + path)
                )
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse());
        assertEquals(orderId, result.getResponse().getContentAsString());
        verify(orderReturningService).completeOrderProductReturn(orderProductId);
    }
}