package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.order.dto.MemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.NonMemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderCrudService;
import com.nhnacademy.book.order.service.OrderProcessService;
import com.nhnacademy.book.order.service.OrderValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderProcessServiceImpl implements OrderProcessService {
    private final OrderCrudService orderCrudService;
    private final OrderValidationService orderValidationService;
    private final OrderCacheService orderCacheService;
    private final OrderDeliveryAddressService orderDeliveryAddressService;
    private final MemberOrderService memberOrderService;
    private final NonMemberOrderService nonMemberOrderService;


    /**
     * 주문요청 처리 (검증, 저장, 캐싱)
     *
     * @param orderRequest 주문요청
     * @param <T>          회원 주문요청 | 비회원 주문요청
     * @return 주문응답 DTO (결제요청을 위한 데이터)
     */
    @Transactional
    @Override
    public <T extends OrderRequestDto> OrderResponseDto processRequestedOrder(T orderRequest) {
        // 주문 검증
        orderValidationService.validateOrder(orderRequest);
        //TODO: 주문재고 차감처리

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
        OrderRequestDto orderRequest = orderCacheService.fetchOrderCache(orderId);
        return completeOrderRequest(orderId, orderRequest);
    }


    private String completeOrderRequest(String orderId, OrderRequestDto orderRequest) {
        // 배송지저장
        orderDeliveryAddressService.addOrderDeliveryAddress(orderId, orderRequest.getOrderDeliveryAddressDto());
        // 주문상품 저장
        // 회원/비회원 주문 저장
        addOrderByMemberType(orderId, orderRequest);
        // TODO: 쿠폰 사용처리
        // TODO: 포인트 사용처리
        // TODO: 재고 차감처리

        return orderId;
    }


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
