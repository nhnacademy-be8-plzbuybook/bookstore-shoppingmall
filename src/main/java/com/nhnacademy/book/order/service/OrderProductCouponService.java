package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;

import java.math.BigDecimal;
import java.util.List;

public interface OrderProductCouponService {
    Long saveOrderProductCoupon(Long orderProductId, List<OrderProductAppliedCouponDto> appliedCoupon);
    BigDecimal calculateCouponDiscount(OrderProductRequestDto orderProduct);
    BigDecimal calculateCouponDiscounts(List<OrderProductRequestDto> orderProducts);
}
