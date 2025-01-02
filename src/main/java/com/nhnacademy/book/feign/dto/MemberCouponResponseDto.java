package com.nhnacademy.book.feign.dto;

import com.nhnacademy.book.feign.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MemberCouponResponseDto(
        Long memberCouponId,
        Long memberId,
        CouponResponseDto coupon
) {
    public record CouponResponseDto(
            Long id,
            String code,
            Status status,
            LocalDateTime issuedAt,
            LocalDateTime expiredAt,
            String name,
            BigDecimal discountLimit,
            Integer discountRatio
    ) {

    }
}
