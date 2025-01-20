package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderStatusServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @InjectMocks
    private OrderStatusServiceImpl orderStatusService;

    @DisplayName("주문상태 수정")
    @Test
    void modifyOrderStatus() {
        String orderId = "orderId";
        OrderStatusModifyRequestDto orderStatusModifyRequestDto = new OrderStatusModifyRequestDto(OrderStatus.DELIVERED);
        Orders order = new Orders(orderId, "orderNumber", "orderName", LocalDate.now(), 0, BigDecimal.valueOf(3000), LocalDateTime.now(), OrderStatus.DELIVERING, BigDecimal.valueOf(6000));
        OrderProduct orderProduct1 = new OrderProduct(1L, new SellingBook(), order, BigDecimal.valueOf(3000), 1, BigDecimal.ZERO, OrderProductStatus.DELIVERING);
        OrderProduct orderProduct2 = new OrderProduct(2L, new SellingBook(), order, BigDecimal.valueOf(3000), 1, BigDecimal.ZERO, OrderProductStatus.DELIVERING);
        order.addOrderProduct(orderProduct1);
        order.addOrderProduct(orderProduct2);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));


        //when
        orderStatusService.modifyOrderStatus(orderId, orderStatusModifyRequestDto);

        //then
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertEquals(OrderProductStatus.DELIVERED, orderProduct1.getStatus());
        assertEquals(OrderProductStatus.DELIVERED, orderProduct2.getStatus());
    }

    @DisplayName("주문상태 수정: 주문 못찾음")
    @Test
    void modifyOrderStatus_order_not_found() {
        String orderId = "orderId";
        OrderStatusModifyRequestDto orderStatusModifyRequestDto = new OrderStatusModifyRequestDto(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());


        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderStatusService.modifyOrderStatus(orderId, orderStatusModifyRequestDto));

        //then
        assertEquals("찾을 수 없는 주문입니다.", exception.getMessage());
    }

    @DisplayName("주문상품 상태 수정")
    @Test
    void modifyOrderProductStatus() {
        Orders order = new Orders("orderId", "orderNumber", "orderName", LocalDate.now(), 0, BigDecimal.valueOf(3000), LocalDateTime.now(), OrderStatus.DELIVERED, BigDecimal.valueOf(6000));
        OrderProduct orderProduct1 = new OrderProduct(1L, new SellingBook(), order, BigDecimal.valueOf(3000), 1, BigDecimal.ZERO, OrderProductStatus.DELIVERED);
        OrderProduct orderProduct2 = new OrderProduct(2L, new SellingBook(), order, BigDecimal.valueOf(3000), 1, BigDecimal.ZERO, OrderProductStatus.DELIVERED);
        OrderProduct orderProduct3 = new OrderProduct(3L, new SellingBook(), order, BigDecimal.valueOf(3000), 1, BigDecimal.ZERO, OrderProductStatus.DELIVERED);
        order.addOrderProduct(orderProduct1);
        order.addOrderProduct(orderProduct2);
        order.addOrderProduct(orderProduct3);
        when(orderProductRepository.findById(3L)).thenReturn(Optional.of(orderProduct3));
        //when
        orderStatusService.modifyOrderProductStatus(3L, OrderProductStatus.RETURN_REQUESTED);

        //then
        assertEquals(OrderProductStatus.DELIVERED, orderProduct1.getStatus());
        assertEquals(OrderProductStatus.DELIVERED, orderProduct2.getStatus());
        assertEquals(OrderProductStatus.RETURN_REQUESTED, orderProduct3.getStatus());
        assertEquals(OrderStatus.RETURN_REQUESTED, order.getStatus());
    }

    @DisplayName("주문상품 상태 수정: 존재하지 않는 주문상품")
    @Test
    void modifyOrderProductStatus_orderProduct_not_found() {
        when(orderProductRepository.findById(3L)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderStatusService.modifyOrderProductStatus(3L, OrderProductStatus.RETURN_REQUESTED));

        //then
        assertEquals("주문상품정보를 찾을 수 없습니다.", exception.getMessage());
    }
}