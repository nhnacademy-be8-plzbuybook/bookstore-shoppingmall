package com.nhnacademy.book.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class PaymentConfirmRequestDto {
    private String paymentKey;
    private String orderId;
    private BigDecimal amount;
    private Integer usedPoint;
}
