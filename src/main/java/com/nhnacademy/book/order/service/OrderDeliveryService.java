package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.entity.Orders;

public interface OrderDeliveryService {
    Long registerOrderDelivery(OrderDeliveryRegisterRequestDto registerRequest);

    boolean isInReturnablePeriod(Orders order);
}
