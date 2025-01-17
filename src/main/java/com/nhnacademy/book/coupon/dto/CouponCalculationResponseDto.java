package com.nhnacademy.book.coupon.dto;

import java.math.BigDecimal;

public record CouponCalculationResponseDto(
        BigDecimal discountAmount, // 할인 금액
        BigDecimal originalPrice, // 할인 적용 전 가격
        BigDecimal calculationPrice // 할인 적용 후 가격
) {
}
