package com.nhnacademy.book.deliveryFeePolicy.dto;


import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

//@EqualsAndHashCode
@Getter
public class DeliveryFeePolicySaveRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal defaultDeliveryFee;
    @NotNull
    private BigDecimal freeDeliveryThreshold;

    public DeliveryFeePolicySaveRequestDto(String name, BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold) {
        this.name = name;
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    public DeliveryFeePolicy toEntity() {
        return new DeliveryFeePolicy(name, defaultDeliveryFee, freeDeliveryThreshold);
    }
}
