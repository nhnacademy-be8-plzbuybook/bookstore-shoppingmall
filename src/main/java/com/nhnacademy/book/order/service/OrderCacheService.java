package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;

public interface OrderCacheService {
    void saveOrderCache(String orderId, OrderRequestDto order);
    OrderRequestDto fetchOrderCache(String orderId);
}
