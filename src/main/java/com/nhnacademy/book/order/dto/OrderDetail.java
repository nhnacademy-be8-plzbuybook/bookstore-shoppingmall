package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.payment.dto.PaymentDto;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderDetail {
    private List<OrderProductDto> orderProducts;
    private OrderDeliveryAddressDto orderDeliveryAddress;
    private PaymentDto payment;

    public OrderDetail(List<OrderProductDto> orderProducts, OrderDeliveryAddressDto orderDeliveryAddress, PaymentDto payment) {
        this.orderProducts = orderProducts;
        this.orderDeliveryAddress = orderDeliveryAddress;
        this.payment = payment;
    }
}
