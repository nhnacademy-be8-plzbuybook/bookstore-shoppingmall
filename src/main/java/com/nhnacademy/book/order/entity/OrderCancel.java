package com.nhnacademy.book.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class OrderCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_cancel_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime canceledAt;

    @Column(length = 500, nullable = false)
    private String cancelReason;

    @OneToOne
    @JoinColumn(name = "oc_order_id", referencedColumnName = "order_id")
    private Orders order;

    public OrderCancel(LocalDateTime canceledAt, String cancelReason, Orders order) {
        this.canceledAt = canceledAt;
        this.cancelReason = cancelReason;
        this.order = order;
    }
}
