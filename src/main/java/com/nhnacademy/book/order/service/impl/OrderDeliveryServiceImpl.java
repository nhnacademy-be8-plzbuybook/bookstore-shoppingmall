package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.order.entity.OrderDelivery;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderDeliveryRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import com.nhnacademy.book.order.service.OrderStatusService;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryService {
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;
    public static final int REFUND_LIMIT_DATE = 10;

    @Transactional
    @Override
    public Long registerOrderDelivery(String orderId, OrderDeliveryRegisterRequestDto registerRequest) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        OrderDelivery savedOrderDelivery = orderDeliveryRepository.save(registerRequest.toEntity(order));
        // 주문상태: 발송완료
        order.updateOrderStatus(OrderStatus.SHIPPED);
        // 주문상품 상태: 배송 중
        order.getOrderProducts().forEach(orderProduct -> orderProduct.updateStatus(OrderProductStatus.SHIPPED));

        return savedOrderDelivery.getId();
    }

    @Override
    public boolean isInReturnablePeriod(Orders order) {
        OrderDelivery orderDelivery = orderDeliveryRepository.findByOrder(order).orElseThrow(() -> new NotFoundException("주문배송정보를 찾을 수 없습니다."));
        Period period = Period.between(orderDelivery.getRegisteredAt().toLocalDate(), LocalDate.now());

        if (!(period.getDays() > REFUND_LIMIT_DATE)) {
            return true;
        }
        return false;
    }


    /**
     * 배송완료 처리
     *
     * @param orderId
     * @param deliveryId
     */
    @Transactional
    @Override
    public void completeOrderDelivery(String orderId, Long deliveryId) {
        // 주문, 주문상품 상태변경
        orderStatusService.modifyOrderStatus(orderId, new OrderStatusModifyRequestDto(OrderStatus.DELIVERED));
        OrderDelivery orderDelivery = orderDeliveryRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundException("주문배송정보를 찾을 수 없습니다."));
        // 주문배송 완료처리
        orderDelivery.completeDelivery();
    }
}
