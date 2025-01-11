package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.entity.OrderDelivery;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderDeliveryRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryService {
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public Long registerOrderDelivery(OrderDeliveryRegisterRequestDto registerRequest) {
        Orders order = orderRepository.findById(registerRequest.getOrderId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        OrderDelivery savedOrderDelivery = orderDeliveryRepository.save(registerRequest.toEntity(order));
        // 주문상태: 발송완료
        order.updateOrderStatus(OrderStatus.SHIPPED);
        // 주문상품 상태: 배송 중
        order.getOrderProducts().forEach(orderProduct -> orderProduct.updateStatus(OrderProductStatus.SHIPPED));

        return savedOrderDelivery.getId();
    }
}
