package com.nhnacademy.book.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class PaymentCancelRequestDto {
    @JsonProperty("cancelReason")
    @NotBlank
    private String reason;

    @JsonProperty("cancelAmount")
    @Nullable
    private BigDecimal cancelAmount;
    @Setter
    private String orderId;
}
