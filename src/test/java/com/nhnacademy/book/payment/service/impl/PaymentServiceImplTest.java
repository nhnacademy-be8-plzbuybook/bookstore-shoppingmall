package com.nhnacademy.book.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.exception.PriceMismatchException;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.exception.PaymentFailException;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import com.nhnacademy.book.payment.service.TossPaymentService;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TossPaymentService tossPaymentService;
    @Mock
    private OrderCacheService orderCacheService;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private PaymentServiceImpl paymentService;
    private PaymentSaveRequestDto saveRequest;

    @BeforeEach
    void setup() {
        String paymentKey = "paymentKey";
        String orderId = "orderId";
        String currency = "won";
        String method = "간편결제";
        BigDecimal totalAmount = BigDecimal.valueOf(100_000);
        OffsetDateTime approvedAt = OffsetDateTime.now();
        PaymentSaveRequestDto.EasyPay easyPay = new PaymentSaveRequestDto.EasyPay("토스페이");
        String status = "DONE";
        PaymentSaveRequestDto.Receipt receipt = new PaymentSaveRequestDto.Receipt("url");
        saveRequest = spy(new PaymentSaveRequestDto(paymentKey, orderId, currency, method, totalAmount, approvedAt, easyPay, status, receipt));
    }


    @DisplayName("결제정보 저장")
    @Test
    void recordPayment() {
        String orderId = "orderId";
        Orders mockOrder = mock(Orders.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        Payment mockPayment = mock(Payment.class);
        when(saveRequest.toEntity(mockOrder)).thenReturn(mockPayment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);
        when(mockPayment.getId()).thenReturn(1L);

        //when
        Long savedPaymentId = paymentService.recordPayment(saveRequest);

        //then
        assertNotNull(savedPaymentId);
        assertEquals(1L, savedPaymentId);
        verify(paymentRepository).save(mockPayment);
    }

    @DisplayName("결제정보 저장: 주문정보 없음")
    @Test
    void recordPayment_order_not_found() {
        String orderId = "orderId";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        //when then
        assertThrows(NotFoundException.class, () -> paymentService.recordPayment(saveRequest));
        verify(orderRepository).findById(saveRequest.getOrderId());
    }

    @DisplayName("결제정보 삭제")
    @Test
    void removePayment() {
        Long paymentId = 1L;
        doNothing().when(paymentRepository).deleteById(paymentId);

        //when
        paymentService.removePayment(paymentId);

        verify(paymentRepository).deleteById(paymentId);

    }

    @DisplayName("결제정보 삭제: paymentId가 null")
    @Test
    void removePayment_paymentId_null() {

        //when then
        assertThrows(IllegalArgumentException.class, () -> paymentService.removePayment(null));
        verify(paymentRepository, never()).deleteById(anyLong());
    }

    @DisplayName("결제검증")
    @Test
    void verifyPayment() {
        Integer usedPoint = 1000;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        BigDecimal deliveryFee = BigDecimal.valueOf(3_000);
        BigDecimal orderPrice = BigDecimal.valueOf(30_000);
        BigDecimal paymentPrice = orderPrice.add(deliveryFee).subtract(couponDiscount).subtract(BigDecimal.valueOf(usedPoint));

        OrderProductRequestDto orderProductRequest = new OrderProductRequestDto(1L, orderPrice, 1, List.of(new OrderProductAppliedCouponDto(1L, couponDiscount)), null);
        OrderRequestDto orderRequest = new OrderRequestDto(
                OrderType.MEMBER_ORDER,
                LocalDate.of(2025, 3, 6),
                usedPoint,
                List.of(orderProductRequest),
                new OrderDeliveryAddressDto("필문대로 123번길", "12345", "조선대학교 1층", "NHN", "010-1234-5678"),
                deliveryFee,
                orderPrice,
                "test@email.com",
                null
        );
        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto("paymentKey", "orderId", paymentPrice, 1000);
        when(orderCacheService.fetchOrderCache(confirmRequest.getOrderId())).thenReturn(orderRequest);

        //when
        paymentService.verifyPayment(confirmRequest);

        //then
        verify(orderCacheService).fetchOrderCache(confirmRequest.getOrderId());
    }

    @DisplayName("결제검증: 주문캐시 못찾음")
    @Test
    void verifyPayment_cant_find_orderCache() {
        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto("paymentKey", "orderId", BigDecimal.valueOf(29_000), 1000);
        when(orderCacheService.fetchOrderCache(anyString())).thenReturn(null);

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> paymentService.verifyPayment(confirmRequest));
        assertEquals("주문 캐시를 찾을 수 없습니다.", exception.getMessage());
        verify(orderCacheService).fetchOrderCache(anyString());
    }

    @DisplayName("결제검증: 결제정보 불일치")
    @Test
    void verifyPayment_orderPayment_not_accorded() {
        Integer usedPoint = 1000;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        BigDecimal deliveryFee = BigDecimal.valueOf(3_000);
        BigDecimal orderPrice = BigDecimal.valueOf(30_000);
        BigDecimal wrongPaymentPrice = BigDecimal.ZERO;

        OrderProductRequestDto orderProductRequest = new OrderProductRequestDto(1L, orderPrice, 1, List.of(new OrderProductAppliedCouponDto(1L, couponDiscount)), null);
        OrderRequestDto orderRequest = new OrderRequestDto(
                OrderType.MEMBER_ORDER,
                LocalDate.of(2025, 3, 6),
                usedPoint,
                List.of(orderProductRequest),
                new OrderDeliveryAddressDto("필문대로 123번길", "12345", "조선대학교 1층", "NHN", "010-1234-5678"),
                deliveryFee,
                orderPrice,
                "test@email.com",
                null
        );
        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto("paymentKey", "orderId", wrongPaymentPrice, 1000);
        when(orderCacheService.fetchOrderCache(confirmRequest.getOrderId())).thenReturn(orderRequest);

        //when then
        PriceMismatchException exception = assertThrows(PriceMismatchException.class, () -> paymentService.verifyPayment(confirmRequest));
        assertEquals("주문결제 정보가 일치하지 않습니다.", exception.getMessage());
        verify(orderCacheService).fetchOrderCache(anyString());
    }


    @DisplayName("주문취소")
    @Test
    void cancelPayment() {
        String orderId = "order123";
        String paymentKey = "paymentKey123";
        ZonedDateTime canceledAt = ZonedDateTime.now();
        BigDecimal cancelAmount = BigDecimal.valueOf(5000);
        String easyPayProvider = "PayProvider";

        Payment existingPayment = Payment.builder()
                .paymentKey(paymentKey)
                .build();

        PaymentCancelRequestDto cancelRequest = new PaymentCancelRequestDto("reason", cancelAmount, orderId);
        Orders order = new Orders();

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("paymentKey", paymentKey);
        mockResponse.put("status", "CANCELED");
        mockResponse.put("method", "CARD");
        LinkedHashMap<String, Object> latestCancel = new LinkedHashMap<>();
        latestCancel.put("canceledAt", canceledAt.toString());
        latestCancel.put("cancelAmount", cancelAmount.intValue());
        LinkedHashMap<String, Object> easyPay = new LinkedHashMap<>();
        easyPay.put("provider", easyPayProvider);
        mockResponse.put("easyPay", easyPay);

        when(paymentRepository.findOldestByOrderId(orderId)).thenReturn(Optional.of(existingPayment));
        when(tossPaymentService.cancelPayment(paymentKey, cancelRequest)).thenReturn(mockResponse);
        when(tossPaymentService.extractLatestCancel(mockResponse)).thenReturn(latestCancel);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(1L, "CANCELED", paymentKey, canceledAt.toLocalDateTime(), cancelAmount, "CARD", easyPayProvider, order));

        // when
        Long savedPaymentId = paymentService.cancelPayment(cancelRequest);

        // then
        assertNotNull(savedPaymentId);
        assertEquals(1L, savedPaymentId);

        verify(paymentRepository).findOldestByOrderId(orderId);
        verify(tossPaymentService).cancelPayment(paymentKey, cancelRequest);
        verify(tossPaymentService).extractLatestCancel(mockResponse);
        verify(orderRepository).findById(orderId);
        verify(paymentRepository).save(any(Payment.class));
    }


    @DisplayName("주문취소: 결제정보 없음")
    @Test
    void cancelPayment_cant_find_payment() {

        PaymentCancelRequestDto cancelRequest = new PaymentCancelRequestDto("reason", BigDecimal.valueOf(30_000), "orderId");
        when(paymentRepository.findOldestByOrderId(anyString())).thenReturn(Optional.empty());

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> paymentService.cancelPayment(cancelRequest));
        verify(paymentRepository).findOldestByOrderId(anyString());
        assertEquals("결제정보를 찾을 수 없습니다.", exception.getMessage());
    }


    @DisplayName("주문취소: 주문정보 없음")
    @Test
    void cancelPayment_cant_find_order() {
        String orderId = "order123";
        String paymentKey = "paymentKey123";
        ZonedDateTime canceledAt = ZonedDateTime.now();
        BigDecimal cancelAmount = BigDecimal.valueOf(5000);
        String easyPayProvider = "PayProvider";

        Payment existingPayment = Payment.builder()
                .paymentKey(paymentKey)
                .build();

        PaymentCancelRequestDto cancelRequest = new PaymentCancelRequestDto("reason", cancelAmount, orderId);
        Orders order = new Orders();

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("paymentKey", paymentKey);
        mockResponse.put("status", "CANCELED");
        mockResponse.put("method", "CARD");
        LinkedHashMap<String, Object> latestCancel = new LinkedHashMap<>();
        latestCancel.put("canceledAt", canceledAt.toString());
        latestCancel.put("cancelAmount", cancelAmount.intValue());
        LinkedHashMap<String, Object> easyPay = new LinkedHashMap<>();
        easyPay.put("provider", easyPayProvider);
        mockResponse.put("easyPay", easyPay);

        when(paymentRepository.findOldestByOrderId(orderId)).thenReturn(Optional.of(existingPayment));
        when(tossPaymentService.cancelPayment(paymentKey, cancelRequest)).thenReturn(mockResponse);
        when(tossPaymentService.extractLatestCancel(mockResponse)).thenReturn(latestCancel);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> paymentService.cancelPayment(cancelRequest));

        // then
        assertEquals("주문정보를 찾을 수 없습니다.", exception.getMessage());
        verify(paymentRepository).findOldestByOrderId(orderId);
        verify(tossPaymentService).cancelPayment(paymentKey, cancelRequest);
        verify(tossPaymentService).extractLatestCancel(mockResponse);
        verify(orderRepository).findById(orderId);
    }

    @DisplayName("결제승인")
    @Test
    void confirmPayment() throws JsonProcessingException {
        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto("paymentKey", "orderId", BigDecimal.valueOf(30_000), 1000);
        JSONObject successResponse = new JSONObject();
        when(tossPaymentService.confirmPayment(confirmRequest)).thenReturn(successResponse);
        when(objectMapper.readValue(anyString(), eq(PaymentSaveRequestDto.class))).thenReturn(saveRequest);
        Orders mockOrder = mock(Orders.class);
        Payment mockPayment = mock(Payment.class);
        when(orderRepository.findById(saveRequest.getOrderId())).thenReturn(Optional.of(mockOrder));
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);
        when(mockPayment.getId()).thenReturn(1L);

        //when
        JSONObject result = paymentService.confirmPayment(confirmRequest);

        assertEquals(successResponse, result);

        verify(paymentRepository).save(any(Payment.class));
        verify(mockOrder).updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
    }


    @DisplayName("결제승인 중 오류 발생")
    @Test
    void confirmPayment_fail() {
        PaymentConfirmRequestDto confirmRequest = new PaymentConfirmRequestDto("paymentKey", "orderId", BigDecimal.valueOf(30_000), 1000);
        JSONObject faildResponse = new JSONObject();
        faildResponse.put("error", 400);
        when(tossPaymentService.confirmPayment(confirmRequest)).thenReturn(faildResponse);

        //when then
        PaymentFailException exception = assertThrows(PaymentFailException.class, () -> paymentService.confirmPayment(confirmRequest));
        assertEquals("결제승인 중 오류가 발생했습니다.", exception.getMessage());
        verify(tossPaymentService).cancelPayment(anyString(), any(PaymentCancelRequestDto.class));
    }


}