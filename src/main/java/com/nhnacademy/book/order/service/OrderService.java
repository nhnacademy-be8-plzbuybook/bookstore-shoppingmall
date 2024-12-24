package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderSaveRequestDto;
import com.nhnacademy.book.order.dto.OrderSaveResponseDto;
import com.nhnacademy.book.order.dto.OrderUpdateRequestDto;
import com.nhnacademy.book.order.entity.Orders;

import java.util.List;

public interface OrderService {
    Orders getOrderById(String orderId);
    List<Orders> getOrders();
    OrderSaveResponseDto createOrder(OrderSaveRequestDto saveRequest);
    String modifyOrder(String orderId, OrderUpdateRequestDto updateRequest);
//    void removeOrder(String orderId);
}
