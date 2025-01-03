package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.MemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.NonMemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.*;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class OrderProcessServiceImpl implements OrderProcessService {
    private final OrderCrudService orderCrudService;
    private final OrderValidationService orderValidationService;
    private final OrderCacheService orderCacheService;
    private final OrderDeliveryAddressService orderDeliveryAddressService;
    private final MemberOrderService memberOrderService;
    private final NonMemberOrderService nonMemberOrderService;
    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;
    private final OrderProductWrappingService orderProductWrappingService;


    /**
     * 주문요청 처리 (검증, 저장, 캐싱)
     *
     * @param orderRequest 주문요청
     * @return 주문응답 DTO (결제요청을 위한 데이터)
     */
    @Transactional
    @Override
    public OrderResponseDto processRequestedOrder(OrderRequestDto orderRequest) {
        // 주문 검증
        orderValidationService.validateOrder(orderRequest);
        // 주문 저장
        OrderResponseDto orderResponseDto = orderCrudService.createOrder(orderRequest);
        // 주문정보 캐싱
        orderCacheService.saveOrderCache(orderResponseDto.getOrderId(), orderRequest);
        return orderResponseDto;
    }


    /**
     * 주문 완료 처리
     *
     * @param orderId 주문 ID
     * @return 주문 ID
     */
    @Transactional
    @Override
    public String completeOrder(String orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
        // 주문캐시정보 가져오기
        OrderRequestDto orderCache = orderCacheService.fetchOrderCache(orderId);

        for (OrderProductRequestDto orderProductRequest : orderCache.getOrderProducts()) {
            // 주문상품 저장
            OrderProduct savedOrderProduct = orderProductService.saveOrderProduct(order, orderProductRequest);
            order.addOrderProduct(savedOrderProduct);

            // 주문상품-포장 저장
            savedOrderProductWrapping(savedOrderProduct, orderProductRequest);
            // TODO: 쿠폰 사용처리
        }
        // 배송지저장
        orderDeliveryAddressService.addOrderDeliveryAddress(orderId, orderCache.getOrderDeliveryAddress());
        // 회원/비회원 주문 저장
        addOrderByMemberType(orderId, orderCache);
        // TODO: 포인트 사용처리

        // 주문상태 "결제완료"로 변경
        order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        return orderId;
    }


    /**
     * 주문상품-포장 저장
     *
     * @param orderProduct 주문상품 요청 DTO
     */
    private void savedOrderProductWrapping(OrderProduct savedOrderProduct, OrderProductRequestDto orderProduct) {
        if (orderProduct.getWrapping() != null) {
            OrderProductWrappingDto orderProductWrapping = orderProduct.getWrapping();
            orderProductWrappingService.saveOrderProductWrapping(savedOrderProduct.getOrderProductId(),
                    orderProductWrapping.getWrappingPaperId(), orderProduct.getQuantity());
        }
    }

    @Override
    public BigDecimal getOrderTotalPrice(OrderRequestDto orderRequest) {
        return null;
    }


    /**
     * 주문타입(회원|비회원) 별로 주문 저장
     *
     * @param orderId 주문 ID
     * @param orderRequest 주문요청 DTO
     */
    private void addOrderByMemberType(String orderId, OrderRequestDto orderRequest) {
        if (orderRequest instanceof MemberOrderRequestDto memberOrderRequestDto) {
            memberOrderService.addMemberOrder(new MemberOrderSaveRequestDto(memberOrderRequestDto.getMemberEmail(), orderId));
        } else if (orderRequest instanceof NonMemberOrderRequestDto nonMemberOrderRequestDto) {
            nonMemberOrderService.addNonMemberOrder(new NonMemberOrderSaveRequestDto(orderId, nonMemberOrderRequestDto.getNonMemberPassword()));
        } else {
            throw new IllegalArgumentException("Invalid order request type");
        }
    }


}
