package com.nhnacademy.book.order.dto;

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
}
