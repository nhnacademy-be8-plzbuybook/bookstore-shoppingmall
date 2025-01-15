package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;

public interface OrderStatusService {
    void modifyOrderStatus(String orderId, OrderStatusModifyRequestDto modifyRequest);
}
