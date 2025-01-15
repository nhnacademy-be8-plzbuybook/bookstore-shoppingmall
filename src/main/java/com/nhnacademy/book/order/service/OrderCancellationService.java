package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.dto.OrderProductCancelRequestDto;

public interface OrderCancellationService {
//    void cancelOrder(String orderId, OrderCancelRequestDto cancelRequest);
    void cancelOrderProduct(String orderId, Long orderProductId, OrderProductCancelRequestDto orderProductCancelRequest);
}
