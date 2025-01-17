package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;

public interface OrderStatusService {
    void modifyOrderStatus(String orderId, OrderStatusModifyRequestDto modifyRequest);
    void modifyOrderProductStatus(Long orderProductId, OrderProductStatus orderProductStatus);
}
