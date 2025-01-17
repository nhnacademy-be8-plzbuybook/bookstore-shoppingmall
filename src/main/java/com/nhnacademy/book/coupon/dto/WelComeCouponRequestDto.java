package com.nhnacademy.book.coupon.dto;


import java.time.LocalDateTime;

// 회원가입 쿠폰 발급 요청 Dto
public record WelComeCouponRequestDto(
        Long memberId, // 회원 고유 ID
        LocalDateTime registeredAt // 회원가입 시간
) {
}
