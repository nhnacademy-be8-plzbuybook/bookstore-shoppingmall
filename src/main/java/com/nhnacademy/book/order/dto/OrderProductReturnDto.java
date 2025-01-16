package com.nhnacademy.book.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderProductReturnDto {
    private Long id;
    private String reason;
    private int quantity;
    private String trackingNumber;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private String orderId;
    private Long orderProductId;

    @QueryProjection
    public OrderProductReturnDto(Long id, String reason, int quantity, String trackingNumber, LocalDateTime requestedAt,
                                 LocalDateTime completedAt, String orderId, Long orderProductId) {
        this.id = id;
        this.reason = reason;
        this.quantity = quantity;
        this.trackingNumber = trackingNumber;
        this.requestedAt = requestedAt;
        this.completedAt = completedAt;
        this.orderId = orderId;
        this.orderProductId = orderProductId;
    }
}
