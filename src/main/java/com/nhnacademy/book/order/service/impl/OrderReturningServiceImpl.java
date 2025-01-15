package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.entity.OrderReturn;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.repository.OrderReturnQueryRepository;
import com.nhnacademy.book.order.repository.OrderReturnRepository;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import com.nhnacademy.book.order.service.OrderReturningService;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderReturningServiceImpl implements OrderReturningService {
    private final OrderRepository orderRepository;
    private final OrderReturnRepository orderReturnRepository;
    private final OrderReturnQueryRepository orderReturnQueryRepository;
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
        // 주문상태변경
        order.getOrderProducts().forEach(op -> op.updateStatus(OrderProductStatus.RETURN_REQUESTED));
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
        order.getOrderProducts().forEach(op -> op.updateStatus(OrderProductStatus.RETURN_COMPLETED));

        // 주문반품 테이블 업데이트
        orderReturn.setCompletedAt(LocalDateTime.now());
        return orderId;
    }

    @Transactional(readOnly = true)
    @Override
    public OrderReturnDto getOrderReturnByTrackingNumber(String trackingNumber) {
        return orderReturnRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("주문반품정보를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderReturnDto> getAllOrderReturns(OrderReturnSearchRequestDto searchRequest, Pageable pageable) {
        Page<OrderReturnDto> orderReturnDtoPage = orderReturnQueryRepository.findOrderReturnPage(searchRequest, pageable);
        return orderReturnDtoPage;
//
//        if (trackingNumber != null && !trackingNumber.isBlank()) {
//            OrderReturnDto orderReturn = orderReturnRepository.findByTrackingNumber(trackingNumber)
//                    .orElseThrow(() -> new NotFoundException("주문반품요청이 없습니다."));
//            return new PageImpl<>(List.of(orderReturn), pageable, 1);
//        }
//        Page<OrderReturn> orderReturnPage = orderReturnRepository.findAll(pageable);
//        return orderReturnPage.map(OrderReturnDto::new);
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
