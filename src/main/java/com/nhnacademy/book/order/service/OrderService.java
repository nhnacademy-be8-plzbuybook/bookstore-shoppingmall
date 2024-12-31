package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.*;
import com.nhnacademy.book.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderDto> getOrders(OrderSearchRequestDto searchRequest, Pageable pageable);
}
