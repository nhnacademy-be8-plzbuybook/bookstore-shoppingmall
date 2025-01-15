package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderCancelRequestDto;

public interface OrderCancellationService {
    void cancelOrder(String orderId, OrderCancelRequestDto cancelRequest);
    void cancelOrderProduct(Long orderProductId, Integer quantity);
}
