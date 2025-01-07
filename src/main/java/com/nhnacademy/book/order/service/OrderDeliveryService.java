package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;

public interface OrderDeliveryService {
    String registerOrderDelivery(OrderDeliveryRegisterRequestDto registerRequest);
}
