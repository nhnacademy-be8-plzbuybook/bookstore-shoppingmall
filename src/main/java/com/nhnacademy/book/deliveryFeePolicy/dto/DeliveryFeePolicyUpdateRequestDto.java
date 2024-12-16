package com.nhnacademy.book.deliveryFeePolicy.dto;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DeliveryFeePolicyUpdateRequestDto(
        @NotNull BigDecimal defaultDeliveryFee,
        @NotNull BigDecimal freeDeliveryThreshold) {
}
