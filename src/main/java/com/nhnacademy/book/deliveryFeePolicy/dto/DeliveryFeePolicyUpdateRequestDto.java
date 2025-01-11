package com.nhnacademy.book.deliveryFeePolicy.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DeliveryFeePolicyUpdateRequestDto(
        @NotBlank String name,
        @NotNull BigDecimal defaultDeliveryFee,
        @NotNull BigDecimal freeDeliveryThreshold) {
}
