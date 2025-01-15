package com.nhnacademy.book.payment.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import com.nhnacademy.book.payment.service.PaymentService;
import com.nhnacademy.book.payment.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderCacheService orderCacheService;
    private final TossPaymentService tossPaymentService;

    @Override
    public String recordPayment(PaymentSaveRequestDto saveRequest) {
        String orderId = saveRequest.getOrderId();
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        Payment payment = saveRequest.toEntity(order);

        Payment savedPayment = paymentRepository.save(payment);


        return orderId;
    }


    @Override
    public void verifyPayment(PaymentConfirmRequestDto confirmRequest) {
        OrderRequestDto orderCache = orderCacheService.fetchOrderCache(confirmRequest.getOrderId());

        if (orderCache == null) {
            throw new NotFoundException("주문 캐시를 찾을 수 없습니다.");
        }

        BigDecimal paymentPrice = orderCache.getOrderPrice().add(orderCache.getDeliveryFee().subtract(BigDecimal.valueOf(orderCache.getUsedPoint() != null ? orderCache.getUsedPoint() : 0)));
        if (confirmRequest.getAmount().compareTo(paymentPrice) != 0) {
            throw new IllegalArgumentException("주문결제 정보가 일치하지 않습니다."); //400
        }
    }


    @Override
    public void cancelPayment(String paymentKey, PaymentCancelRequestDto cancelRequest) {
        Orders order = orderRepository.findById(cancelRequest.getOrderId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        JSONObject jsonObject = tossPaymentService.cancelPayment(paymentKey, cancelRequest);
        //cancels의 마지막 cancel를 저장
        LinkedHashMap<String, Object> latestCancel = tossPaymentService.extractLatestCancel(jsonObject);
        LinkedHashMap<String, Object> easyPay = (LinkedHashMap<String, Object>) jsonObject.get("easyPay");
        ZonedDateTime canceledAt = ZonedDateTime.parse((String) latestCancel.get("canceledAt"));
        Payment payment = Payment.builder()
                .paymentKey((String) jsonObject.get("paymentKey"))
                .status((String) jsonObject.get("status"))
                .method((String) jsonObject.get("method"))
                .recordedAt(canceledAt.toLocalDateTime())
                .amount(BigDecimal.valueOf((Integer)latestCancel.get("cancelAmount")))
                .easyPayProvider((String) easyPay.get("provider"))
                .orders(order)
                .build();
        paymentRepository.save(payment);
    }
}