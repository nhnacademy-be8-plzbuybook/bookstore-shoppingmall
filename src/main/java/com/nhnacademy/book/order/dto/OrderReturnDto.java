package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderReturn;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderReturnDto {
    private Long id;
    private String reason;
    private String trackingNumber;
    private LocalDateTime requestedAt;
    private String orderId;

    public OrderReturnDto(OrderReturn entity) {
        this(
                entity.getId(),
                entity.getReason(),
                entity.getTrackingNumber(),
                entity.getRequestedAt(),
                entity.getOrder().getId()
        );
    }
}
