package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;

public interface OrderCacheService {
    void saveOrderCache(String orderId, OrderRequestDto order);
    OrderRequestDto fetchOrderCache(String orderId);
    Long preemptStockCache(Long productId, Integer quantity);
    void addStockCache(Long productId, Long quantity);
    int getStockCache(Long productId);
    void rollbackOrderedStock(OrderRequestDto orderRequest);
}
