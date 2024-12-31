package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.entity.OrderDeliveryAddress;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.OrderDeliveryAddressRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderDeliveryAddressService {
    private final OrderDeliveryAddressRepository orderDeliveryAddressRepository;
    private final OrderRepository orderRepository;

    public Long addOrderDeliveryAddress(String orderId, OrderDeliveryAddressDto orderDeliveryAddressDto) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다."));
        OrderDeliveryAddress orderDeliveryAddress = orderDeliveryAddressDto.toEntity();
        orderDeliveryAddress.setOrder(order);
        OrderDeliveryAddress saved = orderDeliveryAddressRepository.save(orderDeliveryAddress);
        return saved.getId();
    }
}
