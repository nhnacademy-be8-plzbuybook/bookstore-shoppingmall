package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderDetail;
import com.nhnacademy.book.order.dto.OrderDto;
import com.nhnacademy.book.order.dto.OrderSearchRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.repository.OrderDeliveryAddressRepository;
import com.nhnacademy.book.order.repository.OrderQueryRepository;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.payment.dto.PaymentDto;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderQueryRepository orderQueryRepository;
    private final OrderDeliveryAddressRepository orderDeliveryAddressRepository;
    private final PaymentRepository paymentRepository;

    // 전체 주문 조회
    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> getOrders(OrderSearchRequestDto searchRequest, Pageable pageable) {
        return orderQueryRepository.findOrders(searchRequest.getMemberId(), searchRequest.getProductName(),
                searchRequest.getOrderDate(), searchRequest.getOrderStatus(), pageable);
    }

    public OrderDetail getOrderDetail(String orderId) {
        List<OrderProductDto> orderProducts = orderQueryRepository.findOrderProducts(orderId);
        PaymentDto payment = paymentRepository.findByOrders_Id(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 결제정보입니다."));
        OrderDeliveryAddressDto orderDeliveryAddress = orderDeliveryAddressRepository.findByOrder_Id(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문배송정보입니다."));

        return new OrderDetail(orderProducts, orderDeliveryAddress, payment);
    }

}
