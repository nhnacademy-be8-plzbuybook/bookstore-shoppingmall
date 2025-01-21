package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.entity.OrderDeliveryAddress;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.OrderDeliveryAddressRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDeliveryAddressServiceTest {
    @Mock
    private OrderDeliveryAddressRepository orderDeliveryAddressRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderDeliveryAddressService orderDeliveryAddressService;

    @Test
    void addOrderDeliveryAddress() {
        String orderId = "orderId";
        OrderDeliveryAddressDto orderDeliveryAddressDto = new OrderDeliveryAddressDto("locationAddress", "zipCode", "detailAddress", "recipient", "recipientPhone");
        Orders mockOrder = mock(Orders.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        OrderDeliveryAddress orderDeliveryAddress = new OrderDeliveryAddress(1L, "locationAddress", "zipCode", "detailAddress", "recipient", "recipientPhone", mockOrder);
        when(orderDeliveryAddressRepository.save(any(OrderDeliveryAddress.class))).thenReturn(orderDeliveryAddress);

        //when
        Long result = orderDeliveryAddressService.addOrderDeliveryAddress(orderId, orderDeliveryAddressDto);
        assertNotNull(result);
        assertEquals(1L, result);

        verify(orderDeliveryAddressRepository).save(any(OrderDeliveryAddress.class));
    }
}