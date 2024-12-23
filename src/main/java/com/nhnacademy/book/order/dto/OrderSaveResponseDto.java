package com.nhnacademy.book.order.dto;

import java.math.BigDecimal;

public record OrderSaveResponseDto(String orderId, BigDecimal amount, String orderName) {
}
