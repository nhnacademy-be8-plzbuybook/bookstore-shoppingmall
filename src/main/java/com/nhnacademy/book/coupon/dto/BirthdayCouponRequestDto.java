package com.nhnacademy.book.coupon.dto;

import java.time.LocalDateTime;

public record BirthdayCouponRequestDto(
        Long memberId, // 회원 ID (식별키)
        LocalDateTime registerAt // 등록일
) {
}
