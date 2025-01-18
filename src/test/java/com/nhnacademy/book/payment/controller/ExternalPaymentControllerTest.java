package com.nhnacademy.book.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.service.PaymentService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExternalPaymentController.class)
class ExternalPaymentControllerTest {
    @MockBean
    private PaymentService paymentService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("결제승인")
    @Test
    void confirmPayment() throws Exception {
        String url = "/api/payments/confirm/widget";
        String paymentKey = "paymentKey";
        String orderId = "orderId";
        BigDecimal amount = BigDecimal.valueOf(30_000);
        Integer usedPoint = 1000;

        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto(paymentKey, orderId, amount, usedPoint);

        // Mock 설정
        doNothing().when(paymentService).verifyPayment(any(PaymentConfirmRequestDto.class));
        JSONObject response = new JSONObject();
        response.put("status", "DONE");
        when(paymentService.confirmPayment(any(PaymentConfirmRequestDto.class))).thenReturn(response);

        // 요청 및 검증
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest)))
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @DisplayName("결제승인: payment = blank")
    @Test
    void confirmPayment_blank_payment() throws Exception {
        String url = "/api/payments/confirm/widget";
        String paymentKey = " ";
        String orderId = "orderId";
        BigDecimal amount = BigDecimal.valueOf(30_000);
        Integer usedPoint = 1000;

        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto(paymentKey, orderId, amount, usedPoint);

        // Mock 설정
        doNothing().when(paymentService).verifyPayment(any(PaymentConfirmRequestDto.class));
        JSONObject response = new JSONObject();
        response.put("status", "DONE");
        when(paymentService.confirmPayment(any(PaymentConfirmRequestDto.class))).thenReturn(response);

        // 요청 및 검증
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("결제승인: orderId = blank")
    @Test
    void confirmPayment_blank_orderId() throws Exception {
        String url = "/api/payments/confirm/widget";
        String paymentKey = "paymentKey";
        String orderId = "";
        BigDecimal amount = BigDecimal.valueOf(30_000);
        Integer usedPoint = 1000;

        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto(paymentKey, orderId, amount, usedPoint);

        // Mock 설정
        doNothing().when(paymentService).verifyPayment(any(PaymentConfirmRequestDto.class));
        JSONObject response = new JSONObject();
        response.put("status", "DONE");
        when(paymentService.confirmPayment(any(PaymentConfirmRequestDto.class))).thenReturn(response);

        // 요청 및 검증
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("결제승인: amount = null")
    @Test
    void confirmPayment_null_amount() throws Exception {
        String url = "/api/payments/confirm/widget";
        String paymentKey = "paymentKey";
        String orderId = "orderId";
        BigDecimal amount = null;
        Integer usedPoint = 1000;

        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto(paymentKey, orderId, amount, usedPoint);

        // Mock 설정
        doNothing().when(paymentService).verifyPayment(any(PaymentConfirmRequestDto.class));
        JSONObject response = new JSONObject();
        response.put("status", "DONE");
        when(paymentService.confirmPayment(any(PaymentConfirmRequestDto.class))).thenReturn(response);

        // 요청 및 검증
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("결제승인: usedPoint = -1")
    @Test
    void confirmPayment_minus_point() throws Exception {
        String url = "/api/payments/confirm/widget";
        String paymentKey = "payment";
        String orderId = "orderId";
        BigDecimal amount = BigDecimal.valueOf(30_000);
        Integer usedPoint = -1;

        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto(paymentKey, orderId, amount, usedPoint);

        // Mock 설정
        doNothing().when(paymentService).verifyPayment(any(PaymentConfirmRequestDto.class));
        JSONObject response = new JSONObject();
        response.put("status", "DONE");
        when(paymentService.confirmPayment(any(PaymentConfirmRequestDto.class))).thenReturn(response);

        // 요청 및 검증
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest)))
                .andExpect(status().isBadRequest());
    }

}