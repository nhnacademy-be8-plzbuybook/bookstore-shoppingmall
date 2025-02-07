package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderDelivery;
import com.nhnacademy.book.order.entity.Orders;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderDeliveryRegisterRequestDto {
    @NotBlank
    private String deliveryCompany;
    @NotBlank
    private String trackingNumber;

    public OrderDelivery toEntity(Orders order) {
        return new OrderDelivery(order,deliveryCompany, trackingNumber, LocalDateTime.now());
    }
}
