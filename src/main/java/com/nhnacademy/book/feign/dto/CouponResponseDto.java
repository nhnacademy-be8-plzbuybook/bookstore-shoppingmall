package com.nhnacademy.book.feign.dto;

import com.nhnacademy.book.feign.Status;

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
