package com.nhnacademy.book.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderProductCouponDto {
    private Long couponId;
    private Long orderProductId;
    private BigDecimal discount;

    @QueryProjection
    public OrderProductCouponDto(Long couponId, Long orderProductId, BigDecimal discount) {
        this.couponId = couponId;
        this.orderProductId = orderProductId;
        this.discount = discount;
    }
}
