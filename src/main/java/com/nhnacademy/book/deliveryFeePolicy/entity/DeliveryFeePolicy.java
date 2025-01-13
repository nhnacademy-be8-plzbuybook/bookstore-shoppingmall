package com.nhnacademy.book.deliveryFeePolicy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class DeliveryFeePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_fee_policy_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal defaultDeliveryFee;

    @Column(nullable = false)
    private BigDecimal freeDeliveryThreshold;

    public DeliveryFeePolicy(String name, BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold) {
        this.name = name;
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }


    // dto와 의존성 낮추기 위해
    public void update(String name, BigDecimal defaultDeliveryFee, BigDecimal freeDeliveryThreshold) {
        this.name = name;
        this.defaultDeliveryFee = defaultDeliveryFee;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }
}
