package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;

import java.util.List;

public interface OrderProductCouponService {
    Long saveOrderProductCoupon(Long orderProductId, List<OrderProductAppliedCouponDto> appliedCoupon);
}
