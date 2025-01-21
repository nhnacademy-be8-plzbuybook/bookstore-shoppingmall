package com.nhnacademy.book.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderDeliveryController.class)
class OrderDeliveryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderDeliveryService orderDeliveryService;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "/api/orders";
    @Test
    void registerOrderDelivery() throws Exception{
        String orderId = "orderId";
        String deliveryCompany = "cj";
        String trackingNumber = "1231231234";
        OrderDeliveryRegisterRequestDto orderDeliveryRegisterRequestDto = new OrderDeliveryRegisterRequestDto(deliveryCompany, trackingNumber);

        ArgumentCaptor<OrderDeliveryRegisterRequestDto> captor = ArgumentCaptor.forClass(OrderDeliveryRegisterRequestDto.class);
        when(orderDeliveryService.registerOrderDelivery(orderId, orderDeliveryRegisterRequestDto)).thenReturn(1L);

        String path = "/" + orderId + "/deliveries";
        mockMvc.perform(post(BASE_URL + path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDeliveryRegisterRequestDto)))
                .andExpect(status().isOk());

        verify(orderDeliveryService).registerOrderDelivery(eq(orderId), captor.capture());
    }

    @Test
    void completeOrderDelivery() throws Exception{
        String orderId = "orderId";
        Long deliveryId = 1L;
        String path = "/" + orderId + "/deliveries/" + deliveryId + "/complete";

        doNothing().when(orderDeliveryService).completeOrderDelivery(orderId, deliveryId);

        mockMvc.perform(post(BASE_URL + path)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(orderDeliveryService).completeOrderDelivery(orderId, deliveryId);
    }
}