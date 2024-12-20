package com.nhnacademy.book.payment.entity;

import com.nhnacademy.book.order.entity.Orders;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String method;

    // 토스 주문 id
    @Column(nullable = false)
    private String tossOrderId;

    @Setter
    @OneToOne
    @JoinColumn(referencedColumnName = "order_id")
    private Orders orders;

    @Builder
    public Payment(String paymentKey, LocalDateTime paidAt, BigDecimal amount, String method, String tossOrderId) {
        this.paymentKey = paymentKey;
        this.paidAt = paidAt;
        this.amount = amount;
        this.method = method;
        this.tossOrderId = tossOrderId;
    }


}
