package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.NonMemberOrderDetail;
import com.nhnacademy.book.order.dto.OrderDetail;
import com.nhnacademy.book.order.dto.OrderDto;
import com.nhnacademy.book.order.dto.OrderSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getOrders(OrderSearchRequestDto searchRequest, Pageable pageable);
    OrderDetail getOrderDetail(String orderId);
    NonMemberOrderDetail getNonMemberOrderDetail(String orderNumber, String password);
}
