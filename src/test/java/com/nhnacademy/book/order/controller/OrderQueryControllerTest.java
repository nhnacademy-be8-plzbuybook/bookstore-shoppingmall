package com.nhnacademy.book.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.order.dto.*;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.payment.dto.PaymentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderQueryController.class)
class OrderQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private OrderProductService orderProductService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllOrders() throws Exception {
        // Given
        OrderSearchRequestDto searchRequest = new OrderSearchRequestDto();
        List<OrderDto> orders = List.of(new OrderDto("orderId", "orderNumber", LocalDateTime.now(), OrderStatus.ORDER_CANCELLED, "orderName", BigDecimal.valueOf(30_000), "orderer"));
        Page<OrderDto> orderDtoPage = new PageImpl<>(orders, PageRequest.of(0, 10), orders.size());

        // When
        when(orderService.getOrders(any(OrderSearchRequestDto.class), any(PageRequest.class)))
                .thenReturn(orderDtoPage);

        // Then
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value("orderId"))
                .andExpect(jsonPath("$.content[0].orderName").value("orderName"));
    }

    @Test
    void getMyOrders_Unauthorized() throws Exception {
        // Given
        String memberEmail = null;
        OrderSearchRequestDto searchRequest = new OrderSearchRequestDto();

        // When & Then
        mockMvc.perform(get("/api/orders/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyOrders_Forbidden() throws Exception {
        // Given
        String memberEmail = "test@email.com";
        OrderSearchRequestDto searchRequest = new OrderSearchRequestDto();

        // Mock memberService to return null (member not found)
        when(memberService.getMemberByEmail(memberEmail)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/orders/my")
                        .header("X-USER-ID", memberEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getOrderDetail() throws Exception {
        // Given
        String orderId = "order123";
//        OrderDetail orderDetail = new OrderDetail("orderId", "orderNumber", OrderStatus.DELIVERED, BigDecimal.ZERO, BigDecimal.valueOf(30_000),
//                LocalDate.now(), LocalDateTime.now(), 0, mock(OrderDeliveryAddressDto.class), mock(OrderDeliveryDto.class), mock(PaymentDto.class));
        OrderDetail orderDetail = mock(OrderDetail.class);
        when(orderDetail.getOrderId()).thenReturn(orderId);
        // When
        when(orderService.getOrderDetail(orderId)).thenReturn(orderDetail);

        // Then
        MvcResult result = mockMvc.perform(get("/api/orders/{order-id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse());
        OrderDetail responseContent = objectMapper.readValue(result.getResponse().getContentAsString(), OrderDetail.class);
        assertEquals(orderDetail.getOrderId(), responseContent.getOrderId());
    }

    @Test
    void getNonMemberOrderDetail() throws Exception {
        // Given
        NonMemberOrderDetailAccessRequestDto accessRequestDto = new NonMemberOrderDetailAccessRequestDto("order123", "password");
        String orderId = "order123";

        // When
        when(orderService.getNonMemberOrderId(any(NonMemberOrderDetailAccessRequestDto.class))).thenReturn(orderId);

        // Then
        MvcResult result = mockMvc.perform(post("/api/orders/non-member/access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessRequestDto)))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(result.getResponse());
        assertEquals(orderId, result.getResponse().getContentAsString());
    }

    @Test
    void purchaseConfirm() throws Exception {
        // Given
        Long orderProductId = 1L;

        // When & Then
        mockMvc.perform(put("/api/orders/order-products/{order-product-id}/purchase-confirm", orderProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
