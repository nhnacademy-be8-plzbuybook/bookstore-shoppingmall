package com.nhnacademy.book.payment.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class PaymentConfirmRequestDto {
    @NotBlank
    private String paymentKey;

    @NotBlank
    private String orderId;

    @NotNull
    private BigDecimal amount;

    @Nullable
    @Min(0)
    private Integer usedPoint;
}
