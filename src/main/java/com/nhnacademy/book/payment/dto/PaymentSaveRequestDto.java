package com.nhnacademy.book.payment.dto;

import com.nhnacademy.book.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PaymentSaveRequestDto {
    String paymentKey;
    String tossOrderId;
    String currency;
    String method;
    BigDecimal totalAmount;
    LocalDateTime approvedAt;

    public Payment toEntity() {
        return Payment.builder()
                .paymentKey(paymentKey)
                .tossOrderId(tossOrderId)
                .amount(totalAmount)
                .method(method)
                .paidAt(approvedAt)
                .build();
    }
}
