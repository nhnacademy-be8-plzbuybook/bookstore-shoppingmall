package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.coupon.dto.ValidationCouponCalculationRequestDto;
import com.nhnacademy.book.coupon.service.CouponService;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.repository.OrderProductCouponRepository;
import com.nhnacademy.book.order.service.OrderProductCouponService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderProductCouponServiceImpl implements OrderProductCouponService {
    private final OrderProductRepository orderProductRepository;
    private final OrderProductCouponRepository orderProductCouponRepository;
    private final CouponService couponService;

    @Override
    public Long saveOrderProductCoupon(Long orderProductId, List<OrderProductAppliedCouponDto> appliedCoupons) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문상품입니다."));
        if (appliedCoupons != null) {
            for (OrderProductAppliedCouponDto orderProductAppliedCouponDto : appliedCoupons) {
                // 쿠폰 검증
                Long couponId = orderProductAppliedCouponDto.getCouponId();
                BigDecimal discount = orderProductAppliedCouponDto.getDiscount();
                couponService.validateCouponCalculation(couponId, new ValidationCouponCalculationRequestDto(discount));
                // 쿠폰 사용처리
                couponService.useCoupon(couponId);

                // 주문상품 쿠폰저장
                orderProductCouponRepository.save(orderProductAppliedCouponDto.toEntity(orderProduct));
            }
        }
        return orderProductId;
    }

    @Override
    public BigDecimal calculateCouponDiscounts(List<OrderProductRequestDto> orderProducts) {
        BigDecimal couponDiscounts = BigDecimal.ZERO;
        for (OrderProductRequestDto orderProduct : orderProducts) {
            couponDiscounts = couponDiscounts.add(calculateCouponDiscount(orderProduct));
        }
        return couponDiscounts;
    }

    @Override
    public BigDecimal calculateCouponDiscount(OrderProductRequestDto orderProduct) {
        BigDecimal couponDiscount = BigDecimal.ZERO;
        if (orderProduct.getAppliedCoupons() != null) {
            for (OrderProductAppliedCouponDto appliedCoupon : orderProduct.getAppliedCoupons()) {
                couponDiscount = couponDiscount.add(appliedCoupon.getDiscount());
            }
        }
        return couponDiscount;
    }
}
