package com.nhnacademy.book.deliveryFeePolicy.dto;


import java.math.BigDecimal;

public record DeliveryFeePolicyDto (Long id,
                                    String name,
                                    BigDecimal defaultDeliveryFee,
                                    BigDecimal freeDeliveryThreshold) {
}
