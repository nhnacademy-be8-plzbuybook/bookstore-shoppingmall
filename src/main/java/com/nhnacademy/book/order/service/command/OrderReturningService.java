package com.nhnacademy.book.order.service.command;

import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnRequestDto;

public interface OrderReturningService {
    String requestOrderReturn(String orderId, OrderReturnRequestDto refundRequest);
    String completeOrderReturn(String orderId);
    OrderReturnDto getByTrackingNumber(String trackingNumber);
}
