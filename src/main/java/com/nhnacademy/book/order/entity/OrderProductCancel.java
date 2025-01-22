package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class OrderProductCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_cancel_id")
    private Long id;

    @Column(length = 500, nullable = false)
    private String reason;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime canceledAt;

    @ManyToOne
    @JoinColumn(name = "opc_order_product_id", referencedColumnName = "order_product_id")
    private OrderProduct orderProduct;

    @Builder
    public OrderProductCancel(Long id, String reason, int quantity, LocalDateTime canceledAt, OrderProduct orderProduct) {
        this.id = id;
        this.reason = reason;
        this.quantity = quantity;
        this.canceledAt = canceledAt;
        this.orderProduct = orderProduct;
    }
}
