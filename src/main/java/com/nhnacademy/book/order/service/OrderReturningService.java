package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderReturningService {
    String requestOrderReturn(String orderId, OrderReturnRequestDto refundRequest);
    String completeOrderReturn(String orderId);
    OrderReturnDto getOrderReturnByTrackingNumber(String trackingNumber);
    Page<OrderReturnDto> getAllOrderReturns(String trackingNumber, Pageable pageable);
}
