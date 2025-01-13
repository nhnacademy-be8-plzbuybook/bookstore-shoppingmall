package com.nhnacademy.book.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class OrderDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_delivery_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "od_order_id", referencedColumnName = "order_id")
    private Orders order;

    private String deliveryCompany;

    private String trackingNumber;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    public OrderDelivery(Orders order, String deliveryCompany, String trackingNumber, LocalDateTime registeredAt) {
        this.order = order;
        this.deliveryCompany = deliveryCompany;
        this.trackingNumber = trackingNumber;
        this.registeredAt = registeredAt;
    }

    public void completeDelivery() {
        this.completedAt = LocalDateTime.now();
    }
}
