package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.*;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.point.service.MemberPointService;
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
    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;
    private final OrderProductWrappingService orderProductWrappingService;
    private final MemberPointService memberPointService;
    private final OrderProductCouponService orderProductCouponService;
    private final CustomerOrderService customerOrderService;

    /**
     * 주문요청 처리 (검증, 저장, 캐싱)
     *
     * @param orderRequest 주문요청
     * @return 주문응답 DTO (결제요청을 위한 데이터)
     */
    @Transactional
    @Override
    public OrderResponseDto requestOrder(OrderRequestDto orderRequest) {
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
        OrderRequestDto orderRequest = orderCacheService.fetchOrderCache(orderId);

        for (OrderProductRequestDto orderProductRequest : orderRequest.getOrderProducts()) {
            // 주문상품 저장
            OrderProduct savedOrderProduct = orderProductService.saveOrderProduct(order, orderProductRequest);
            order.addOrderProduct(savedOrderProduct);

            // 주문상품-포장 저장
            orderProductWrappingService.saveOrderProductWrapping(savedOrderProduct.getOrderProductId(), orderProductRequest.getWrapping());
            // TODO: 쿠폰 사용처리
            orderProductCouponService.saveOrderProductCoupon(savedOrderProduct.getOrderProductId(), orderProductRequest.getAppliedCoupons());
        }

        // TODO: 포인트 사용처리
        Integer usedPoint = orderRequest.getUsedPoint();
        if (usedPoint != null && usedPoint > 0) {
            memberPointService.usedPoint(orderRequest instanceof MemberOrderRequestDto
                            ? ((MemberOrderRequestDto) orderRequest).getMemberEmail()
                            : null,
                    usedPoint);
        }


        // 배송지저장
        orderDeliveryAddressService.addOrderDeliveryAddress(orderId, orderRequest.getOrderDeliveryAddress());
        // 회원/비회원 주문 저장
//        addOrderByMemberType(orderId, orderCache);
        customerOrderService.placeCustomerOrder(orderId, orderRequest);

        // 주문상태 "결제완료"로 변경
        order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);
        // 주문상품 상태 변경
        order.getOrderProducts().forEach(op -> op.updateStatus(OrderProductStatus.PAYMENT_COMPLETED));

        return orderId;
    }

}