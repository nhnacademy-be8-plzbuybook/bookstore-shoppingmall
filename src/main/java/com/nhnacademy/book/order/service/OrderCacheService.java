package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;

import java.util.List;
import java.util.Map;

public interface OrderCacheService {
    void saveOrderCache(String orderId, OrderRequestDto order);

    OrderRequestDto fetchOrderCache(String orderId);

    Long preemptStockCache(Long productId, Integer quantity);

    void addStockCache(Long productId, Long quantity);

    int getStockCache(Long productId);

    int getProductStockCache(Long productId);

    Long getWrappingPaperStockCache(Long wrappingPaperId);

    Map<String, Integer> preemptOrderStock(String orderId, OrderRequestDto orderRequest);
    Map<String, Integer> preemptOrderStock(String orderId, List<OrderProductRequestDto> orderProducts);

    //    void rollbackOrderedStock(OrderRequestDto orderRequest);
    void rollbackOrderedStock(String orderId);
}
