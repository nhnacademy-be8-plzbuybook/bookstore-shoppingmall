package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.entity.Orders;

public interface OrderDeliveryService {
    Long registerOrderDelivery(String orderId, OrderDeliveryRegisterRequestDto registerRequest);

    void completeOrderDelivery(String orderId, Long deliveryId);

    boolean isInReturnablePeriod(Orders order);



}
