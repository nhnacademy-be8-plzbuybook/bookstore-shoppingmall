package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderStatusService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    @Override
    public void modifyOrderStatus(String orderId, OrderStatusModifyRequestDto modifyRequest) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        // 주문상태변경
        order.updateOrderStatus(modifyRequest.getStatus());
        // 주문상품 상태변경
        modifyOrderProductsStatus(order, modifyRequest.getStatus());

    }

    @Transactional
    @Override
    public void modifyOrderProductStatus(Long orderProductId, OrderProductStatus orderProductStatus) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(() -> new NotFoundException("주문상품정보를 찾을 수 없습니다."));
        orderProduct.updateStatus(orderProductStatus);
        Orders order = orderProduct.getOrder();
        order.updateOrderStatus(OrderStatus.fromOrderProductStatus(order.getOrderProducts().stream().map(OrderProduct::getStatus).toList()));
    }

    private void modifyOrderProductsStatus(Orders order, OrderStatus orderStatus) {
        List<OrderProduct> orderProducts = order.getOrderProducts();
        if (orderProducts != null) {
            for (OrderProduct orderProduct : orderProducts) {
                orderProduct.updateStatus(OrderProductStatus.fromStatus(orderStatus.getStatus()));

            }
        }
    }
}
