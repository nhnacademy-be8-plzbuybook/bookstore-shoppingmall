package com.nhnacademy.book.payment.service.impl;

import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.webClient.TossPaymentClient;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TossPaymentServiceImplTest {
    @Mock
    private TossPaymentClient tossPaymentClient;
    @InjectMocks
    private TossPaymentServiceImpl tossPaymentService;

    @DisplayName("결제취소")
    @Test
    void cancelPayment() {
        String paymentKey = "paymentKey";
        PaymentCancelRequestDto cancelRequest = new PaymentCancelRequestDto("reason", BigDecimal.valueOf(10_000), "orderId");

        // Mock 반환값 설정
        JSONObject mockResponse = mock(JSONObject.class);
        when(tossPaymentClient.cancelPayment(anyString(), eq(paymentKey), eq(cancelRequest))).thenReturn(mockResponse);

        // 서비스 메서드 호출
        JSONObject response = tossPaymentService.cancelPayment(paymentKey, cancelRequest);

        // 검증
        verify(tossPaymentClient).cancelPayment(anyString(), eq(paymentKey), eq(cancelRequest));
        assertNotNull(response);
        assertEquals(response, mockResponse);
    }



    @DisplayName("결제확인")
    @Test
    void confirmPayment() {
        String paymentKey = "paymentKey";
        String orderId = "orderId";
        BigDecimal amount = BigDecimal.valueOf(10_000);
        Integer usedPoint = 1_000;
        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto(paymentKey, orderId, amount, usedPoint);
        JSONObject mockResponse = mock(JSONObject.class);
        when(tossPaymentService.confirmPayment(confirmRequest)).thenReturn(mockResponse);

        JSONObject response = tossPaymentService.confirmPayment(confirmRequest);

        verify(tossPaymentClient).confirmPayment(anyString(), eq(confirmRequest));
        assertEquals(mockResponse, response);

    }

    @DisplayName("결제정보에서 최근 취소정보 추출")
    @Test
    void extractLatestCancel() {
        JSONObject mockResponse = new JSONObject();
        LinkedHashMap<String, Object> cancel1 = new LinkedHashMap<>();
        cancel1.put("cancelAmount", 500);

        LinkedHashMap<String, Object> cancel2 = new LinkedHashMap<>();
        cancel2.put("cancelAmount", 300);

        mockResponse.put("cancels", new ArrayList<>(List.of(cancel1, cancel2)));

        LinkedHashMap<String, Object> latestCancel = tossPaymentService.extractLatestCancel(mockResponse);

        assertNotNull(latestCancel);
        assertEquals(300, latestCancel.get("cancelAmount"));
    }

    @Test
    void extractLatestCancel_error() {
        JSONObject mockResponse = mock(JSONObject.class);

        assertThrows(RuntimeException.class, () -> tossPaymentService.extractLatestCancel(mockResponse));
        verify(mockResponse).get(anyString());
    }
}