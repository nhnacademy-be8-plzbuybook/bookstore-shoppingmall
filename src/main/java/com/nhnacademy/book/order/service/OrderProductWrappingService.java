package com.nhnacademy.book.order.service;


import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;

public interface OrderProductWrappingService {
    void saveOrderProductWrapping(long orderProductId, OrderProductWrappingDto orderProductWrapping);
}
