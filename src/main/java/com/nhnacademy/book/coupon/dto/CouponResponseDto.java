package com.nhnacademy.book.coupon.dto;

import com.nhnacademy.book.coupon.Status;

import java.time.LocalDateTime;

public record CouponResponseDto(
        Long id,
        String code,
        Status status,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt,
        Long policyId
) {
}
