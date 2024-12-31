package com.nhnacademy.book.order.service;


public interface OrderProductWrappingService {
    long saveOrderProductWrapping(Long orderProductId, Long wrappingPaperId, int quantity);
}
