package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.entity.OrderDelivery;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.OrderDeliveryRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryService {
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderRepository orderRepository;

    @Override
    public Long registerOrderDelivery(OrderDeliveryRegisterRequestDto registerRequest) {
        Orders order = orderRepository.findById(registerRequest.getOrderId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        OrderDelivery savedOrderDelivery = orderDeliveryRepository.save(registerRequest.toEntity(order));

        return savedOrderDelivery.getId();
    }
}
