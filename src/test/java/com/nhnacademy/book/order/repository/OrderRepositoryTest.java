package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TestEntityManager entityManager;
    private Orders existingOrder;

    @BeforeEach
    void setUp() {
        String orderId = "0130d93d-66c3-4cde-9ba7-72b87600a328";
        String orderNumber = "ORD-250117-1501156054-8718";
        String orderName = "통신선로기능사 필기";
        LocalDateTime orderedAt = LocalDateTime.of(2025, 1, 17, 15, 1, 16);
        Integer usedPoint = 0;
        BigDecimal deliveryFee = BigDecimal.ZERO;
        LocalDate deliveryWishDate = LocalDate.now();
        BigDecimal orderPrice = BigDecimal.valueOf(29_000);
        OrderStatus status = OrderStatus.PAYMENT_COMPLETED;

        Orders order = Orders.builder()
                .id(orderId)
                .number(orderNumber)
                .name(orderName)
                .orderedAt(orderedAt)
                .usedPoint(usedPoint)
                .deliveryFee(deliveryFee)
                .deliveryWishDate(deliveryWishDate)
                .orderPrice(orderPrice)
                .status(status)
                .build();
        existingOrder = orderRepository.save(order);
    }

    @DisplayName("주문저장")
    @Test
    void saveOrder() {
        String orderId = "0130d93d-66c3-4cde-9ba7-72b87600a328";
        String orderNumber = "ORD-250117-1501156054-8711";
        String orderName = "통신선로기능사 필기";
        LocalDateTime orderedAt = LocalDateTime.of(2025, 1, 17, 15, 1, 16);
        Integer usedPoint = 0;
        BigDecimal deliveryFee = BigDecimal.ZERO;
        LocalDate deliveryWishDate = LocalDate.now();
        BigDecimal orderPrice = BigDecimal.valueOf(29_000);
        OrderStatus status = OrderStatus.PAYMENT_COMPLETED;

        Orders order = Orders.builder()
                .id(orderId)
                .number(orderNumber)
                .name(orderName)
                .orderedAt(orderedAt)
                .usedPoint(usedPoint)
                .deliveryFee(deliveryFee)
                .deliveryWishDate(deliveryWishDate)
                .orderPrice(orderPrice)
                .status(status)
                .build();

        orderRepository.save(order);

        entityManager.flush();
        entityManager.clear();

        //when
        Optional<Orders> optionalOrder = orderRepository.findById(orderId);

        //then
        assertTrue(optionalOrder.isPresent());
        Orders savedOrder = optionalOrder.get();
        assertEquals(orderId, savedOrder.getId());
        assertEquals(orderName, savedOrder.getName());
        assertEquals(orderNumber, savedOrder.getNumber());
        assertEquals(orderedAt, savedOrder.getOrderedAt());
        assertEquals(0, savedOrder.getOrderPrice().compareTo(orderPrice));
        assertEquals(0, savedOrder.getDeliveryFee().compareTo(deliveryFee));
        assertEquals(status, savedOrder.getStatus());
        assertEquals(usedPoint, savedOrder.getUsedPoint());
        assertEquals(deliveryWishDate, savedOrder.getDeliveryWishDate());
    }

    @Disabled
    @DisplayName("주문저장: 주문번호중복")
    @Test
    void saveOrder_duplicated_number() {
        String orderId = "0130d93d-66c3-4cde-9ba7-72b87600a328";
        String orderNumber = "ORD-250117-1501156054-8718";
        String orderName = "통신선로기능사 필기";
        LocalDateTime orderedAt = LocalDateTime.of(2025, 1, 17, 15, 1, 16);
        Integer usedPoint = 0;
        BigDecimal deliveryFee = BigDecimal.ZERO;
        LocalDate deliveryWishDate = LocalDate.now();
        BigDecimal orderPrice = BigDecimal.valueOf(29_000);
        OrderStatus status = OrderStatus.PAYMENT_COMPLETED;

        Orders order = Orders.builder()
                .id(orderId)
                .number(orderNumber)
                .name(orderName)
                .orderedAt(orderedAt)
                .usedPoint(usedPoint)
                .deliveryFee(deliveryFee)
                .deliveryWishDate(deliveryWishDate)
                .orderPrice(orderPrice)
                .status(status)
                .build();

        orderRepository.save(order);

        entityManager.flush();
        entityManager.clear();

        //when then
        assertThrows(DataIntegrityViolationException.class, ()
                -> orderRepository.findById(orderId));
    }

    @DisplayName("주문 단건조회")
    @Test
    void findOrder() {
        String orderId = "0130d93d-66c3-4cde-9ba7-72b87600a328";
        String orderNumber = "ORD-250117-1501156054-8718";
        String orderName = "통신선로기능사 필기";
        LocalDateTime orderedAt = LocalDateTime.of(2025, 1, 17, 15, 1, 16);
        Integer usedPoint = 0;
        BigDecimal deliveryFee = BigDecimal.ZERO;
        LocalDate deliveryWishDate = LocalDate.now();
        BigDecimal orderPrice = BigDecimal.valueOf(29_000);
        OrderStatus status = OrderStatus.PAYMENT_COMPLETED;

        Optional<Orders> optionalOrder = orderRepository.findById(orderId);
        assertTrue(optionalOrder.isPresent());
        Orders foundOrder = optionalOrder.get();
        assertEquals(orderId, foundOrder.getId());
        assertEquals(orderName, foundOrder.getName());
        assertEquals(orderNumber, foundOrder.getNumber());
        assertEquals(orderedAt, foundOrder.getOrderedAt());
        assertEquals(0, foundOrder.getOrderPrice().compareTo(orderPrice));
        assertEquals(0, foundOrder.getDeliveryFee().compareTo(deliveryFee));
        assertEquals(status, foundOrder.getStatus());
        assertEquals(usedPoint, foundOrder.getUsedPoint());
        assertEquals(deliveryWishDate, foundOrder.getDeliveryWishDate());
    }

}