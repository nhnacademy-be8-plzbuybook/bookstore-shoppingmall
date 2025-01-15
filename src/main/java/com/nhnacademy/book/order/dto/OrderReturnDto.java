package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderReturn;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderReturnDto {
    private Long id;
    private String reason;
    private String trackingNumber;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private String orderId;

    @QueryProjection
    public OrderReturnDto(Long id, String reason, String trackingNumber, LocalDateTime requestedAt, LocalDateTime completedAt, String orderId) {
        this.id = id;
        this.reason = reason;
        this.trackingNumber = trackingNumber;
        this.requestedAt = requestedAt;
        this.completedAt = completedAt;
        this.orderId = orderId;
    }


    public OrderReturnDto(OrderReturn entity) {
        this(
                entity.getId(),
                entity.getReason(),
                entity.getTrackingNumber(),
                entity.getRequestedAt(),
                entity.getCompletedAt(),
                entity.getOrder().getId()
        );
    }
}
