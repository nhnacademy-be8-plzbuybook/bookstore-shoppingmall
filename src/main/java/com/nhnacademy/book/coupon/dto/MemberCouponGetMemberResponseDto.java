package com.nhnacademy.book.coupon.dto;

import java.time.LocalDateTime;

public record MemberCouponGetMemberResponseDto(
        Long memberCouponId,
        String couponCode,
        String status,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt
) {
}
