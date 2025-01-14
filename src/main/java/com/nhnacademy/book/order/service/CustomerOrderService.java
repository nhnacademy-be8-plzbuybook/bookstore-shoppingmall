package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.CustomerOrderRequestDto;

public interface CustomerOrderService {
    Long placeOrder(CustomerOrderRequestDto orderRequest);
}
