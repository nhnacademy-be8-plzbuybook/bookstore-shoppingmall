package com.nhnacademy.book.feign.dto;

import java.time.LocalDateTime;

public record BirthdayCouponRequestDto(
        Long memberId,
        LocalDateTime registerAt
) {
}
