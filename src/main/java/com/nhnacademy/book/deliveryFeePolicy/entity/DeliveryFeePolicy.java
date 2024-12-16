package com.nhnacademy.book.deliveryFeePolicy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Entity
public class DeliveryFeePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_fee_policy_id")
    private Long id;

    @Column(nullable = false)
    private BigDecimal defaultDeliveryFee;

    @Column(nullable = false)
    private BigDecimal freeDeliveryThreshold;

    public DeliveryFeePolicy(Long id, BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold) {
        this.id = id;
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    public DeliveryFeePolicy(BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold) {
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }


    // dto와 의존성 낮추기 위해
    public void update(BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold) {
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }
}
