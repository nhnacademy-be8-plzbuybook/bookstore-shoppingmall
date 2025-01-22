package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.order.repository.OrderProductCancelRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.order.service.ReturnPointService;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class OrderCancellationServiceImplTest {
    @Mock
    private OrderCacheService orderCacheService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private SellingBookRepository sellingBookRepository;
    @Mock
    private OrderProductCancelRepository orderProductCancelRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidationService orderValidationService;
    @Mock
    private ReturnPointService returnPointService;
    @InjectMocks
    private OrderCancellationServiceImpl orderCancellationService;

    @Test
    void cancelOrderProducts() {
    }
}