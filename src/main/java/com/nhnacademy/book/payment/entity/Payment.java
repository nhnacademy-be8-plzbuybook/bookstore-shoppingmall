package com.nhnacademy.book.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    private String paymentKey;
    private LocalDateTime paidAt;
    private BigDecimal amount;
    private Long orderId;


    public Payment(String paymentKey, LocalDateTime paidAt, BigDecimal amount, Long orderId) {
        this.paymentKey = paymentKey;
        this.paidAt = paidAt;
        this.amount = amount;
        this.orderId = orderId;
    }
}
