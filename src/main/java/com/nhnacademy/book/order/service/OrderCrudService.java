package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderDto;

public interface OrderCrudService {
//    OrderResponseDto createOrder(OrderRequestDto orderRequest);
    OrderResponseDto createOrder(ValidatedOrderDto orderRequest);
}
