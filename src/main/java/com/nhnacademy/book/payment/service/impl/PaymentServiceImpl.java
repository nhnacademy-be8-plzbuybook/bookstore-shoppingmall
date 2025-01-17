package com.nhnacademy.book.payment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.exception.PaymentFailException;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import com.nhnacademy.book.payment.service.PaymentService;
import com.nhnacademy.book.payment.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ObjectMapper objectMapper;

    @Override
    public Long recordPayment(PaymentSaveRequestDto saveRequest) {
        Orders order = orderRepository.findById(saveRequest.getOrderId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        Payment payment = paymentRepository.save(saveRequest.toEntity(order));

        return payment.getId();
    }

    @Override
    public void removePayment(Long paymentId) {
        if (paymentId == null) {
            throw new IllegalArgumentException("결제 id는 null일 수 없습니다.");
        }
        paymentRepository.deleteById(paymentId);
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
    public void cancelPayment(PaymentCancelRequestDto cancelRequest) {
        String orderId = cancelRequest.getOrderId();
        String paymentKey = paymentRepository.findOldestPaymentKeyByOrdersId(orderId).orElseThrow(() -> new NotFoundException("결제정보를 찾을 수 없습니다."));


        PaymentCancelRequestDto paymentCancelRequest = new PaymentCancelRequestDto(cancelRequest.getReason(), cancelRequest.getCancelAmount(), orderId);
        JSONObject jsonObject = tossPaymentService.cancelPayment(paymentKey, paymentCancelRequest);

        LinkedHashMap<String, Object> latestCancel = tossPaymentService.extractLatestCancel(jsonObject);
        LinkedHashMap<String, Object> easyPay = (LinkedHashMap<String, Object>) jsonObject.get("easyPay");
        ZonedDateTime canceledAt = ZonedDateTime.parse((String) latestCancel.get("canceledAt"));
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));

        paymentRepository.save(Payment.builder()
                .paymentKey((String) jsonObject.get("paymentKey"))
                .status((String) jsonObject.get("status"))
                .method((String) jsonObject.get("method"))
                .recordedAt(canceledAt.toLocalDateTime())
                .amount(BigDecimal.valueOf((Integer) latestCancel.get("cancelAmount")))
                .easyPayProvider((String) easyPay.get("provider"))
                .orders(order)
                .build()
        );
    }

    @Transactional
    @Override
    public JSONObject confirmPayment(PaymentConfirmRequestDto confirmRequest) {
        try {
            JSONObject response = tossPaymentService.confirmPayment(confirmRequest);
            if (!isPaymentSuccess(response)) {
                throw new PaymentFailException("결제가 실패했습니다.");
            }
            Long paymentId = recordPayment(convertResponseToDto(response));
            return response;
        } catch (Exception e) {
            // 결제취소
            tossPaymentService.cancelPayment(confirmRequest.getPaymentKey(), new PaymentCancelRequestDto("결제 중 오류발생", null, confirmRequest.getOrderId()));
            throw new PaymentFailException("결제승인 중 오류가 발생했습니다.");
        }
    }

    private PaymentSaveRequestDto convertResponseToDto(JSONObject response) throws JsonProcessingException {
        String jsonString = response.toString();
        return objectMapper.readValue(jsonString, PaymentSaveRequestDto.class);
    }

    private boolean isPaymentSuccess(JSONObject response) {
        int statusCode = response.containsKey("error") ? 400 : 200;
        return statusCode == 200;
    }
}