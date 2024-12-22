package com.nhnacademy.book.order.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public record OrderSaveResponseDto(String orderId, BigDecimal amount, String orderName) {
}
