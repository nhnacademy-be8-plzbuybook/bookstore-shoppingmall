package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderReturn;
import com.nhnacademy.book.order.entity.Orders;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderReturnRequestDto {
    @NotBlank
    private String reason;
    @NotBlank
    private String trackingNumber; // 이거 어떻게?

    public OrderReturn toEntity(Orders order) {
        return OrderReturn.builder()
                .reason(reason)
                .trackingNumber(trackingNumber)
                .requestedAt(LocalDateTime.now())
                .order(order)
                .build();
    }
}
