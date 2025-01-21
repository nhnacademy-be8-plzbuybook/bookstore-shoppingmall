package com.nhnacademy.book.coupon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ValidationCouponCalculationRequestDto(
        @NotNull
        @DecimalMin("0.0")
        BigDecimal price // 주문상품 가격
) {
}
