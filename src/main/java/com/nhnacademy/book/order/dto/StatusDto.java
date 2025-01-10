package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StatusDto {
    private OrderStatus status;

    public StatusDto(OrderStatus status) {
        this.status = status;
    }

    public StatusDto(String status) {
        this.status = OrderStatus.fromStatus(status);
    }
}
