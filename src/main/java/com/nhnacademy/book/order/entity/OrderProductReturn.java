package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class OrderProductReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_return_id", nullable = false)
    private Long id;

    @Column(length = 500, nullable = false)
    private String reason;

    @Column(nullable = false)
    private String trackingNumber;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "opr_order_product_id", referencedColumnName = "order_product_id")
    private OrderProduct orderProduct;

    @Builder
    public OrderProductReturn(Long id, String reason, String trackingNumber, Integer quantity, LocalDateTime requestedAt,
                              LocalDateTime completedAt, OrderProduct orderProduct) {
        this.id = id;
        this.reason = reason;
        this.trackingNumber = trackingNumber;
        this.quantity = quantity;
        this.requestedAt = requestedAt;
        this.completedAt = completedAt;
        this.orderProduct = orderProduct;
    }

    public void complete() {
        this.completedAt = LocalDateTime.now();
    }
}
