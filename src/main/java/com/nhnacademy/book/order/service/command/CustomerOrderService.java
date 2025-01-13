package com.nhnacademy.book.order.service.command;

import com.nhnacademy.book.order.dto.CustomerOrderRequestDto;

public interface CustomerOrderService {
    Long placeOrder(CustomerOrderRequestDto orderRequest);
}
