package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.payment.dto.PaymentDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class NonMemberOrderDetail {
    private String orderId;
    @Setter
    private List<OrderProductDto> orderProducts;
    private BigDecimal deliveryFee;
    private BigDecimal orderPrice;
    private LocalDateTime orderedAt;
    private LocalDate deliveryWishDate;
    private OrderStatus status;
    private String orderNumber;
    private OrderDeliveryAddressDto orderDeliveryAddress;
    private OrderDeliveryDto orderDelivery;
    private PaymentDto payment;
    private String password;


    @Builder
    @QueryProjection
    public NonMemberOrderDetail(String orderId,
                                String orderNumber,
                                OrderStatus status,
                                BigDecimal deliveryFee,
                                BigDecimal orderPrice,
                                LocalDate deliveryWishDate,
                                LocalDateTime orderedAt,
                                String password,
                                OrderDeliveryAddressDto orderDeliveryAddress,
                                OrderDeliveryDto orderDelivery,
                                PaymentDto payment) {

        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.status = status;
        this.deliveryFee = deliveryFee;
        this.orderPrice = orderPrice;
        this.deliveryWishDate = deliveryWishDate;
        this.orderedAt = orderedAt;
        this.password = password;
        this.orderDeliveryAddress = orderDeliveryAddress;
        this.orderDelivery = orderDelivery;
        this.payment = payment;
    }
}
