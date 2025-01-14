package com.nhnacademy.book.order.service.command.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnRequestDto;
import com.nhnacademy.book.order.entity.OrderReturn;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.repository.OrderReturnRepository;
import com.nhnacademy.book.order.service.command.OrderDeliveryService;
import com.nhnacademy.book.order.service.command.OrderReturningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OrderReturningServiceImpl implements OrderReturningService {
    private final OrderRepository orderRepository;
    private final OrderReturnRepository orderReturnRepository;
    private final OrderDeliveryService orderDeliveryService;


    /**
     * 반품요청(사용자)
     *
     * @param orderId
     * @param returnRequest
     * @return
     */
    @Transactional
    @Override
    public String requestOrderReturn(String orderId, OrderReturnRequestDto returnRequest) {
        // 반품요청 조건 확인
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));
        validateOrderOrderForReturning(order);

        //주문반품 저장
        orderReturnRepository.save(returnRequest.toEntity(order));

        // 주문상태변경
        order.updateOrderStatus(OrderStatus.RETURN_REQUESTED);
        return order.getId();
    }


    /**
     * 반품요청 승인(관리자)
     *
     * @param orderId
     * @return
     */
    @Transactional
    @Override
    public String completeOrderReturn(String orderId) {
        //반품조건 확인
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));
        OrderReturn orderReturn = orderReturnRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundException("반품정보를 찾을 수 없습니다."));
        validateOrderForReturnCompletion(order);
        //TODO: 반품 포인트적립 (결제금액 - 반품 택배비)
        //TODO: 재고 복구

        // 주문상태 변경
        order.updateOrderStatus(OrderStatus.RETURN_COMPLETED);

        // 주문반품 테이블 업데이트
        orderReturn.setCompletedAt(LocalDateTime.now());
        return orderId;
    }

    @Transactional(readOnly = true)
    @Override
    public OrderReturnDto getByTrackingNumber(String trackingNumber) {
        return orderReturnRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("주문반품정보를 찾을 수 없습니다."));
    }

    private void validateOrderOrderForReturning(Orders order) {
        int statusCode = order.getStatus().getCode();
        // 발송완료 <= statusCode <= 배송완료
        if (!(statusCode >= 2 && statusCode <= 4)) {
            throw new ConflictException("반품이 불가능한 주문입니다. (사유: 반품가능 상태가 아님)");
        }
        boolean isReturnable = orderDeliveryService.isInReturnablePeriod(order);
        if (!isReturnable) {
            throw new ConflictException("반품이 불가능한 주문입니다. (사유: 반품기간 지남)");
        }
    }

    private void validateOrderForReturnCompletion(Orders order) {
        if (order.getStatus() != OrderStatus.RETURN_REQUESTED) {
            throw new ConflictException("반품요청된 주문이 아닙니다.");
        }
    }
}
