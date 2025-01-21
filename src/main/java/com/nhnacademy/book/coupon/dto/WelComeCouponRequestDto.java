package com.nhnacademy.book.coupon.dto;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record WelComeCouponRequestDto(
        @NotNull
        Long memberId, // 회원 고유 ID
        @NotNull
        LocalDateTime registeredAt // 회원가입 시간
) {
}
