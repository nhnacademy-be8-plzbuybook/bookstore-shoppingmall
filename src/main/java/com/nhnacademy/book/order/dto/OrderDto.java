package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.enums.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class OrderDto {
    private String id;
    // 주문일
    private LocalDateTime orderedAt;
    // 주문상태
    private OrderStatus orderStatus;
    private String orderNumber;
    // 주문명
    private String orderName;
    // 결제금액
    private BigDecimal paymentAmount;
    private String orderer;

    @QueryProjection
    public OrderDto(String id, String orderNumber, LocalDateTime orderedAt, OrderStatus orderStatus, String orderName, BigDecimal paymentAmount, String orderer) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderedAt = orderedAt;
        this.orderStatus = orderStatus;
        this.orderName = orderName;
        this.paymentAmount = paymentAmount;
        this.orderer = orderer;
    }
}
