package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.*;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.payment.service.PaymentService;
import com.nhnacademy.book.point.service.MemberPointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class OrderProcessServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderCrudService orderCrudService;
    @Mock
    private OrderValidationService orderValidationService;
    @Mock
    private OrderCacheService orderCacheService;
    @Mock
    private OrderDeliveryAddressService orderDeliveryAddressService;
    @Mock
    private OrderProductService orderProductService;
    @Mock
    private OrderProductWrappingService orderProductWrappingService;
    @Mock
    private MemberPointService memberPointService;
    @Mock
    private OrderProductCouponService orderProductCouponService;
    @Mock
    private CustomerOrderService customerOrderService;
    @Mock
    private PaymentService paymentService;
    @InjectMocks
    private OrderProcessServiceImpl orderProcessService;

    @Test
    void requestOrder() {
    }

    @Test
    void completeOrder() {
    }
}