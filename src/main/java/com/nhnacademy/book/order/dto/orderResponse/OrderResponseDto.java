package com.nhnacademy.book.order.dto.orderResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@AllArgsConstructor
@Getter
public class OrderResponseDto {
    private String orderId;
    private BigDecimal amount;
    private String orderName;
}
