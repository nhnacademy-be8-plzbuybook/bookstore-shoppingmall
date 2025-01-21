package com.nhnacademy.book.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.order.dto.OrderProductStatusPatchRequestDto;
import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderStatusController.class)
class OrderStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderProductService orderProductService;
    @MockBean
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "/api/orders";

    @Test
    void getOrderStatuses() throws Exception {
        String path = "/order-status";
        MvcResult result = mockMvc.perform(get(BASE_URL + path)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // 검증
        assertNotNull(result.getResponse());
        List<String> orderStatusStrings = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);

        // OrderStatus enum 값을 문자열로 변환하여 비교
        List<String> expectedStatuses = Arrays.stream(OrderStatus.values())
                .map(Enum::name)
                .toList();

        assertEquals(expectedStatuses, orderStatusStrings);
    }

    @Test
    void patchOrderStatus() throws Exception {
        String orderId = "orderId";
        OrderStatusModifyRequestDto statusModifyRequest = new OrderStatusModifyRequestDto(OrderStatus.ORDER_CANCELLED);
        String path = "/" + orderId + "/status";

        doNothing().when(orderService).orderDelivered(orderId);

        //when
        mockMvc.perform(put(BASE_URL + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusModifyRequest))
                )
                .andExpect(status().isNoContent());

        verify(orderService, never()).orderDelivered(orderId);
        verify(orderService).modifyStatus(eq(orderId), any(OrderStatusModifyRequestDto.class));
    }

    @Test
    void patchOrderStatus_delivered() throws Exception {
        String orderId = "orderId";
        OrderStatusModifyRequestDto statusModifyRequest = new OrderStatusModifyRequestDto(OrderStatus.DELIVERED);
        String path = "/" + orderId + "/status";

        doNothing().when(orderService).orderDelivered(orderId);

        //when
        mockMvc.perform(put(BASE_URL + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusModifyRequest))
                )
                .andExpect(status().isNoContent());

        verify(orderService).orderDelivered(orderId);
        verify(orderService, never()).modifyStatus(eq(orderId), any(OrderStatusModifyRequestDto.class));
    }

    @Test
    void patchOrderProductsStatus() throws Exception {
        Long orderProductId = 1L;
        String path = "/order-products/" + orderProductId + "/status";

        OrderProductStatusPatchRequestDto patchRequest = new OrderProductStatusPatchRequestDto(OrderProductStatus.PAYMENT_COMPLETED);
        doNothing().when(orderProductService).patchStatus(orderProductId, patchRequest);

        //when
        mockMvc.perform(put(BASE_URL + path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest))
                )
                .andExpect(status().isNoContent());

        verify(orderProductService).patchStatus(eq(orderProductId), any(OrderProductStatusPatchRequestDto.class));
    }
}