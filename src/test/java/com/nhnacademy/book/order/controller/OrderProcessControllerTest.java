package com.nhnacademy.book.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.service.OrderProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderProcessController.class)
class OrderProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderProcessService orderProcessService;
    private static final String BASE_URL = "/api/orders";
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new OrderProcessController(orderProcessService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
    }

    @Test
    void requestOrder_member() throws Exception {
        // Arrange
        String memberEmail = "test@email.com";
        OrderRequestDto orderRequestDto = new OrderRequestDto(OrderType.MEMBER_ORDER, LocalDate.now(),
                0, List.of(mock(OrderProductRequestDto.class)), mock(OrderDeliveryAddressDto.class), BigDecimal.ZERO,
                BigDecimal.valueOf(30_000), null, null);

        OrderResponseDto orderResponseDto = new OrderResponseDto("orderId", BigDecimal.valueOf(30_000), "수학의 정석 외 2건");
        when(orderProcessService.requestOrder(any(OrderRequestDto.class))).thenReturn(orderResponseDto);

        // Act
        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", memberEmail)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        ArgumentCaptor<OrderRequestDto> captor = ArgumentCaptor.forClass(OrderRequestDto.class);
        verify(orderProcessService).requestOrder(captor.capture());
        OrderRequestDto capturedRequest = captor.getValue();

        assertNotNull(capturedRequest);
        assertEquals(memberEmail, capturedRequest.getMemberEmail());

        assertNotNull(result.getResponse());
        OrderResponseDto responseContent = objectMapper.readValue(result.getResponse().getContentAsString(), OrderResponseDto.class);
        assertEquals(orderResponseDto.getOrderId(), responseContent.getOrderId());
        assertEquals(orderResponseDto.getOrderName(), responseContent.getOrderName());
        assertEquals(orderResponseDto.getAmount(), responseContent.getAmount());
    }


    @Test
    void requestOrder_non_member() throws Exception{
        // Arrange
        String memberEmail = "test@email.com";
        OrderRequestDto orderRequestDto = new OrderRequestDto(OrderType.NON_MEMBER_ORDER, LocalDate.now(),
                0, List.of(mock(OrderProductRequestDto.class)), mock(OrderDeliveryAddressDto.class), BigDecimal.ZERO,
                BigDecimal.valueOf(30_000), null, null);

        OrderResponseDto orderResponseDto = new OrderResponseDto("orderId", BigDecimal.valueOf(30_000), "수학의 정석 외 2건");
        when(orderProcessService.requestOrder(any(OrderRequestDto.class))).thenReturn(orderResponseDto);

        // Act
        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        ArgumentCaptor<OrderRequestDto> captor = ArgumentCaptor.forClass(OrderRequestDto.class);
        verify(orderProcessService).requestOrder(captor.capture());
        OrderRequestDto capturedRequest = captor.getValue();

        assertNotNull(capturedRequest);
        assertNull(capturedRequest.getMemberEmail());

        assertNotNull(result.getResponse());
        OrderResponseDto responseContent = objectMapper.readValue(result.getResponse().getContentAsString(), OrderResponseDto.class);
        assertEquals(orderResponseDto.getOrderId(), responseContent.getOrderId());
        assertEquals(orderResponseDto.getOrderName(), responseContent.getOrderName());
        assertEquals(orderResponseDto.getAmount(), responseContent.getAmount());
    }

    @Test
    void completeOrder() throws Exception {
        String orderId = "orderId";

        String path = "/" + orderId + "/complete";

        when(orderProcessService.completeOrder(orderId)).thenReturn(orderId);

        //when
        MvcResult result = mockMvc.perform(post(BASE_URL + path))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse());
        assertEquals(orderId, result.getResponse().getContentAsString());
        //then
        verify(orderProcessService).completeOrder(orderId);
    }
}