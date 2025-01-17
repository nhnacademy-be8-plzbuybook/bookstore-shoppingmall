package com.nhnacademy.book.coupon.dto;

import java.time.LocalDateTime;

public record BirthdayCouponRequestDto(
        Long memberId,
        LocalDateTime registerAt
) {
}
