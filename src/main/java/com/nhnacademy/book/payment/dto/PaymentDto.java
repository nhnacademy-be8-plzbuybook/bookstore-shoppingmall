package com.nhnacademy.book.payment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PaymentDto {
    private BigDecimal amount;
    private String method;
    private String easyPayProvider;
    private LocalDateTime recordedAt;

    @QueryProjection
    public PaymentDto(BigDecimal amount, String method, String easyPayProvider, LocalDateTime recordedAt) {
        this.amount = amount;
        this.method = method;
        this.easyPayProvider = easyPayProvider;
        this.recordedAt = recordedAt;
    }
}
