package com.nhnacademy.book.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Entity
public class OrderReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_return_id")
    private Long id;

    @Column(length = 500, nullable = false)
    private String reason;

    @Column(length = 30, unique = true, nullable = false)
    private String trackingNumber;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Setter
    private LocalDateTime completedAt;

    @OneToOne
    @JoinColumn(name = "or_order_id", referencedColumnName = "order_id")
    private Orders order;

    @Builder
    public OrderReturn(Long id, String reason, String trackingNumber, LocalDateTime requestedAt, LocalDateTime completedAt, Orders order) {
        this.id = id;
        this.reason = reason;
        this.trackingNumber = trackingNumber;
        this.requestedAt = requestedAt;
        this.completedAt = completedAt;
        this.order = order;
    }

}
