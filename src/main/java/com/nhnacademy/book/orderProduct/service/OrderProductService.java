package com.nhnacademy.book.orderProduct.service;

import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;

public interface OrderProductService {
    OrderProduct saveOrderProduct(Orders order, OrderProductRequestDto orderProductRequest);
    //TODO: 주문상품 상태변경
    //TODO: 주문상품 취소
    //TODO: 주문상품 반품요청

}
