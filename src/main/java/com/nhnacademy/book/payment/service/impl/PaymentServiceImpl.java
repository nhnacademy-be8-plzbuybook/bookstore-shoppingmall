package com.nhnacademy.book.payment.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import com.nhnacademy.book.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String SAVE_AMOUNT_KEY = "save_amount:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final PaymentRepository paymentRepository;
    private final OrderCacheService orderCacheService;

    @Override
    public String recordPayment(PaymentSaveRequestDto saveRequest) {
        Payment payment = saveRequest.toEntity();
        Payment savedPayment = paymentRepository.save(payment);

        return savedPayment.getOrderId();
    }

    @Override
    public Payment getByOrderId(String orderId) {
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);
        return optionalPayment.orElseThrow(() -> new NotFoundException("can not found payment info"));
    }

    @Override
    public void verifyPayment(PaymentConfirmRequestDto confirmRequest) {
        OrderRequestDto orderCache = orderCacheService.fetchOrderCache(confirmRequest.getOrderId());

        if (orderCache == null) {
            throw new NotFoundException("주문 캐시를 찾을 수 없습니다.");
        }

        BigDecimal paymentPrice = calculatePaymentPrice(orderCache);
        if (confirmRequest.getAmount().compareTo(paymentPrice) != 0) {
            throw new IllegalArgumentException("주문결제 정보가 일치하지 않습니다."); //400
        }
    }

    private BigDecimal calculatePaymentPrice(OrderRequestDto order) {
        BigDecimal couponDiscount = calculateCouponDiscount(order);
        BigDecimal orderPrice = calculateOrderPrice(order);
        return orderPrice.subtract(couponDiscount).subtract(BigDecimal.valueOf(order.getUsedPoint()));
    }

    private BigDecimal calculateOrderPrice(OrderRequestDto order) {
        BigDecimal orderPrice = BigDecimal.ZERO;

        for (OrderProductRequestDto orderProduct : order.getOrderProducts()) {
            // 주문상품 가격 계산
            orderPrice = orderPrice.add(orderProduct.getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())));
            // 주문상품 포장지 가격 계산
            if (orderProduct.getWrapping() != null) {
                orderPrice = orderPrice.add(orderProduct.getWrapping().getPrice().multiply(BigDecimal.valueOf(orderProduct.getWrapping().getQuantity())));
            }
        }
        return orderPrice;
    }

    private BigDecimal calculateCouponDiscount(OrderRequestDto order) {
        BigDecimal couponDiscount = BigDecimal.ZERO;

        for (OrderProductRequestDto orderProduct : order.getOrderProducts()) {
            // 주문상품 적용 쿠폰 계산
            if (orderProduct.getAppliedCoupons() != null && !orderProduct.getAppliedCoupons().isEmpty()) {
                for (OrderProductAppliedCouponDto coupon : orderProduct.getAppliedCoupons()) {
                    couponDiscount = couponDiscount.add(coupon.getDiscount());
                }
            }
        }
        return couponDiscount;
    }
}