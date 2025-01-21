package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderStatusModifyRequestDto {
    private OrderStatus status;
}
