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
public class OrderDetail {
    private String orderId;
    @Setter
    private List<OrderProductDto> orderProducts;
    private BigDecimal deliveryFee;
    private BigDecimal orderPrice;
    private LocalDateTime orderedAt;
    private LocalDate deliveryWishDate;
    private String status;
    private int usedPoint;
    private BigDecimal couponDiscount;
    private String orderNumber;
    private OrderDeliveryAddressDto orderDeliveryAddress;
    private OrderDeliveryDto orderDelivery;
    private PaymentDto payment;


//    @QueryProjection
//    @Builder
//    public OrderDetail(List<OrderProductDto> orderProducts, OrderDeliveryAddressDto orderDeliveryAddress, PaymentDto payment,
//                       BigDecimal deliveryFee, LocalDateTime orderedAt, LocalDate deliveryWishDate, OrderStatus status,
//                       int usedPoint, BigDecimal couponDiscount, String orderNumber) {
//        this.orderProducts = orderProducts;
//        this.orderDeliveryAddress = orderDeliveryAddress;
//        this.payment = payment;
//        this.deliveryFee = deliveryFee;
//        this.orderedAt = orderedAt;
//        this.deliveryWishDate = deliveryWishDate;
//        this.status = status;
//        this.usedPoint = usedPoint;
//        this.couponDiscount = couponDiscount;
//        this.orderNumber = orderNumber;
//    }


    @Builder
    @QueryProjection
    public OrderDetail(String orderId,
                       String orderNumber,
                       OrderStatus status,
                       BigDecimal deliveryFee,
                       BigDecimal orderPrice,
                       LocalDate deliveryWishDate,
                       LocalDateTime orderedAt,
                       int usedPoint,
                       OrderDeliveryAddressDto orderDeliveryAddress,
                       OrderDeliveryDto orderDelivery,
                       PaymentDto payment) {

        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.status = status.getStatus();
        this.deliveryFee = deliveryFee;
        this.orderPrice = orderPrice;
        this.deliveryWishDate = deliveryWishDate;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
        this.orderDeliveryAddress = orderDeliveryAddress;
        this.orderDelivery = orderDelivery;
        this.payment = payment;
    }
}
