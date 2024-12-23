package com.nhnacademy.book.feign.dto;


// 회원가입 쿠폰 발급 요청 Dto
public record WelComeCouponRequestDto(
        Long memberId, // 회원 고유 ID
        String memberName // 회원 이름
) {
}
