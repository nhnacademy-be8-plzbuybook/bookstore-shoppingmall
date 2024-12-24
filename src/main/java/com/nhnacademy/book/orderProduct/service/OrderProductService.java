package com.nhnacademy.book.orderProduct.service;

import com.nhnacademy.book.orderProduct.dto.OrderProductSaveRequestDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;

public interface OrderProductService {
    OrderProduct saveOrderProduct(OrderProductSaveRequestDto orderProductSaveRequest);
    //TODO: 주문상품 상태변경
    //TODO: 주문상품 취소
    //TODO: 주문상품 반품요청

}
