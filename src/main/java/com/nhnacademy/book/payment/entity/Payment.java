package com.nhnacademy.book.payment.entity;

import com.nhnacademy.book.order.entity.Orders;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, length = 200)
    private String paymentKey;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(length = 50, nullable = false)
    private String method;

    @Column(length = 100)
    private String easyPayProvider;

    @Setter
    @OneToOne
    @JoinColumn( name = "p_order_id", referencedColumnName = "order_id")
    private Orders orders;

    @Builder
    public Payment(Long id, String status, String paymentKey, LocalDateTime paidAt, BigDecimal amount,
                   String method, String easyPayProvider, Orders orders) {
        this.id = id;
        this.status = status;
        this.paymentKey = paymentKey;
        this.paidAt = paidAt;
        this.amount = amount;
        this.method = method;
        this.easyPayProvider = easyPayProvider;
        this.orders = orders;
    }
}
