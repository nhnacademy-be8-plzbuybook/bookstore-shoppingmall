package com.nhnacademy.book.deliveryFeePolicy.dto;


import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

//@EqualsAndHashCode
@Getter
public class DeliveryFeePolicySaveRequestDto {
    @NotNull
    private BigDecimal defaultDeliveryFee;
    @NotNull
    private BigDecimal freeDeliveryThreshold;

    public DeliveryFeePolicySaveRequestDto(BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold) {
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    public DeliveryFeePolicy toEntity() {
        return new DeliveryFeePolicy(defaultDeliveryFee, freeDeliveryThreshold);
    }
}
