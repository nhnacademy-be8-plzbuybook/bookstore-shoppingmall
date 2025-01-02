package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderDto;

public interface OrderCacheService {
    void saveOrderCache(String orderId, OrderRequestDto order);
    void saveOrderCache(String orderId, ValidatedOrderDto order);
    OrderRequestDto fetchOrderCache(String orderId);
    void preemptStockCache(Long productId, Integer quantity);
    void addStockCache(Long productId, Integer quantity);
    int getStockCache(Long productId);
}
