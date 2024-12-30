package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;

public interface OrderValidationService {
    void validateOrder(OrderRequestDto order);
    void validateOrderProduct(OrderProductRequestDto orderProduct);
}
