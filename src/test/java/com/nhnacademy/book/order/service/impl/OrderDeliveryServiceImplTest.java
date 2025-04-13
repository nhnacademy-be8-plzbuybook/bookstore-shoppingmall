//package com.nhnacademy.book.order.service.impl;
//
//import com.nhnacademy.book.book.entity.SellingBook;
//import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
//import com.nhnacademy.book.order.dto.OrderDeliveryRegisterRequestDto;
//import com.nhnacademy.book.order.dto.OrderStatusModifyRequestDto;
//import com.nhnacademy.book.order.entity.OrderDelivery;
//import com.nhnacademy.book.order.entity.Orders;
//import com.nhnacademy.book.order.enums.OrderStatus;
//import com.nhnacademy.book.order.repository.OrderDeliveryRepository;
//import com.nhnacademy.book.order.repository.OrderRepository;
//import com.nhnacademy.book.order.service.OrderStatusService;
//import com.nhnacademy.book.orderProduct.entity.OrderProduct;
//import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class OrderDeliveryServiceImplTest {
//    @Mock
//    private OrderDeliveryRepository orderDeliveryRepository;
//    @Mock
//    private OrderRepository orderRepository;
//    @Mock
//    private OrderStatusService orderStatusService;
//    @InjectMocks
//    private OrderDeliveryServiceImpl orderDeliveryService;
//
//    @Test
//    void registerOrderDelivery() {
//        String orderId = "orderId";
//        String deliveryCompany = "cj";
//        String trackingNumber = "123123123";
//        OrderDeliveryRegisterRequestDto registerRequest = new OrderDeliveryRegisterRequestDto(deliveryCompany, trackingNumber);
//
//        Orders order = new Orders(orderId, "orderNumber", "orderName", LocalDate.now(), 0, BigDecimal.valueOf(3000), LocalDateTime.now(), OrderStatus.DELIVERING, BigDecimal.valueOf(6000));
//        OrderProduct orderProduct1 = new OrderProduct(1L, new SellingBook(), order, BigDecimal.valueOf(3000), 1, BigDecimal.ZERO, OrderProductStatus.DELIVERING);
//        OrderProduct orderProduct2 = new OrderProduct(2L, new SellingBook(), order, BigDecimal.valueOf(3000), 1, BigDecimal.ZERO, OrderProductStatus.DELIVERING);
//        order.addOrderProduct(orderProduct1);
//        order.addOrderProduct(orderProduct2);
//        OrderDelivery mockOrderDelivery = mock(OrderDelivery.class);
//
//        when(mockOrderDelivery.getId()).thenReturn(1L);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//        when(orderDeliveryRepository.save(any(OrderDelivery.class))).thenReturn(mockOrderDelivery);
//
//        //when
//        Long result = orderDeliveryService.registerOrderDelivery(orderId, registerRequest);
//
//        assertNotNull(result);
//        assertEquals(1L, result);
//        assertEquals(OrderStatus.SHIPPED, order.getStatus());
//        assertEquals(OrderProductStatus.SHIPPED, orderProduct1.getStatus());
//        assertEquals(OrderProductStatus.SHIPPED, orderProduct2.getStatus());
//
//        verify(orderDeliveryRepository).save(any(OrderDelivery.class));
//    }
//
//    @Test
//    void registerOrderDelivery_order_not_found() {
//        String orderId = "orderId";
//        String deliveryCompany = "cj";
//        String trackingNumber = "123123123";
//        OrderDeliveryRegisterRequestDto registerRequest = new OrderDeliveryRegisterRequestDto(deliveryCompany, trackingNumber);
//
//        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
//
//        //when
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> orderDeliveryService.registerOrderDelivery(orderId, registerRequest));
//
//        //then
//        assertEquals("찾을 수 없는 주문입니다.", exception.getMessage());
//        verify(orderDeliveryRepository, never()).save(any(OrderDelivery.class));
//    }
//
//    @DisplayName("반품기간검증: 기간 지남")
//    @Test
//    void isInReturnablePeriod_over_period() {
//        String orderId = "orderId";
//        Orders order = new Orders(orderId, "orderNumber", "orderName", LocalDate.now(), 0,
//                BigDecimal.valueOf(3000), LocalDateTime.of(2023,1,1,1,1,1), OrderStatus.DELIVERING, BigDecimal.valueOf(6000));
//        OrderDelivery mockOrderDelivery = mock(OrderDelivery.class);
//        when(mockOrderDelivery.getRegisteredAt()).thenReturn(LocalDateTime.of(2023,1,1,1,1,1));
//        when(orderDeliveryRepository.findByOrder(any(Orders.class))).thenReturn(Optional.of(mockOrderDelivery));
//
//        //when
//        boolean result = orderDeliveryService.isInReturnablePeriod(order);
//
//        assertFalse(result);
//    }
//
//    @DisplayName("반품기간검증: 기간 이내")
//    @Test
//    void isInReturnablePeriod_true() {
//        String orderId = "orderId";
//        Orders order = new Orders(orderId, "orderNumber", "orderName", LocalDate.now(), 0,
//                BigDecimal.valueOf(3000), LocalDateTime.now(), OrderStatus.DELIVERING, BigDecimal.valueOf(6000));
//        OrderDelivery mockOrderDelivery = mock(OrderDelivery.class);
//        when(mockOrderDelivery.getRegisteredAt()).thenReturn(LocalDateTime.now());
//        when(orderDeliveryRepository.findByOrder(any(Orders.class))).thenReturn(Optional.of(mockOrderDelivery));
//
//        //when
//        boolean result = orderDeliveryService.isInReturnablePeriod(order);
//
//        assertTrue(result);
//    }
//
//    @DisplayName("반품기간 검증: 주문배송정보 없음")
//    @Test
//    void isInReturnablePeriod() {
//        String orderId = "orderId";
//        Orders order = new Orders(orderId, "orderNumber", "orderName", LocalDate.now(), 0,
//                BigDecimal.valueOf(3000), LocalDateTime.of(2023,1,1,1,1,1), OrderStatus.DELIVERING, BigDecimal.valueOf(6000));
//        when(orderDeliveryRepository.findByOrder(any(Orders.class))).thenReturn(Optional.empty());
//
//        //when
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> orderDeliveryService.isInReturnablePeriod(order));
//
//        assertEquals("주문배송정보를 찾을 수 없습니다.", exception.getMessage());
//    }
//
//
//    @Test
//    @DisplayName("주문배송완료 처리: 주문배송정보 없음")
//    void completeOrderDelivery_delivery_not_found() {
//        // Given
//        String orderId = "orderId";
//        Long deliveryId = 1L;
//
//        // Mocking: 주문배송정보가 없는 경우
//        when(orderDeliveryRepository.findByOrderId(orderId)).thenReturn(Optional.empty());
//
//        // When & Then: NotFoundException이 발생하는지 확인
//        NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> orderDeliveryService.completeOrderDelivery(orderId, deliveryId));
//
//        assertEquals("주문배송정보를 찾을 수 없습니다.", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("주문배송완료 처리: 정상 처리")
//    void completeOrderDelivery_success() {
//        // Given
//        String orderId = "orderId";
//        Long deliveryId = 1L;
//        OrderDelivery mockOrderDelivery = mock(OrderDelivery.class);
//
//        // Mocking: 주문배송정보가 있는 경우
//        when(orderDeliveryRepository.findByOrderId(orderId)).thenReturn(Optional.of(mockOrderDelivery));
//
//        // Mocking: 주문 상태 변경 메서드
//        doNothing().when(orderStatusService).modifyOrderStatus(eq(orderId), any(OrderStatusModifyRequestDto.class));
//
//        // When
//        orderDeliveryService.completeOrderDelivery(orderId, deliveryId);
//
//        // Then: 메서드가 호출되었는지 검증
//        verify(orderStatusService, times(1))
//                .modifyOrderStatus(eq(orderId), eq(new OrderStatusModifyRequestDto(OrderStatus.DELIVERED)));
//        verify(orderDeliveryRepository, times(1)).findByOrderId(orderId);
//        verify(mockOrderDelivery, times(1)).completeDelivery();
//    }
//}