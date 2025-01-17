package com.nhnacademy.book.coupon.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponPolicyResponseDto(
        Long id, // 쿠폰정책 ID
        String name, // 쿠폰이름
        String saleType, // 할인타입 ( enum 에서 string 으로 교체해서 반환)
        BigDecimal minimumAmount, // 쿠폰적용최소금액
        BigDecimal discountLimit, // 최대할인금액
        Integer discountRatio, // 할인비율
        boolean isStackable, // 쿠폰 중복사용여부 (하나의 주문상품에 여러 쿠폰을 적용가능여부)
        String couponScope, // 쿠폰적용범위
        LocalDateTime startDate, // 쿠폰사용시작일
        LocalDateTime endDate, // 쿠폰사용종료일
        boolean couponActive // 쿠폰정책 활성화 여부
) {
}
