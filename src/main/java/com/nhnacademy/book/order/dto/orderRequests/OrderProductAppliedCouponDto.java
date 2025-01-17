package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.order.entity.OrderProductCoupon;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderProductAppliedCouponDto {
    private Long couponId;
    private BigDecimal discount;

    public OrderProductCoupon toEntity(OrderProduct orderProduct) {
        return new OrderProductCoupon(couponId, orderProduct, discount);
    }
}
