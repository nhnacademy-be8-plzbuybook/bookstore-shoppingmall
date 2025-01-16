package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderCancelRequestDto;

public interface OrderCancellationService {
    void cancelOrderProducts(String orderId, OrderCancelRequestDto cancelRequest);
}
