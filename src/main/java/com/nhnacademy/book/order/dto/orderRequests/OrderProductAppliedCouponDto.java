package com.nhnacademy.book.order.dto.orderRequests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderProductAppliedCouponDto {
    private Long couponId;
    private BigDecimal discount;
}
