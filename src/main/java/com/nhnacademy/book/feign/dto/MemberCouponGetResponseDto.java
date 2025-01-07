package com.nhnacademy.book.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCouponGetResponseDto {
    private Long id; // 쿠폰의 식별 ID
    private String code; // 쿠폰 코드
    private String status; // 쿠폰 상태(미사용, 사용완료, 기한만료, 취소)
    private LocalDateTime issuedAt; // 쿠폰 발급날짜
    private LocalDateTime expiredAt; // 쿠폰 만료날짜
    private String name; // 쿠폰정책 이름(예: Welcome 쿠폰)
    private String saleType; // 할인타입(amount, ratio)
    private BigDecimal minimumAmount; // 쿠폰적용 최소금액
    private BigDecimal discountLimit; // 최대할인 금액
    private Integer discountRatio; // 할인율
    private boolean isStackable; // 쿠폰중복 사용가능 여부
    private String couponScope; // 쿠폰적용 범위
}
