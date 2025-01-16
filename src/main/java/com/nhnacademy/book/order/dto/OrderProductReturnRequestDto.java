package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class OrderProductReturnRequestDto {
    @NotBlank
    private String reason;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotBlank
    private String trackingNumber;

    public OrderProductReturn toEntity(OrderProduct orderProduct) {
        return OrderProductReturn.builder()
                .reason(reason)
                .quantity(quantity)
                .trackingNumber(trackingNumber)
                .requestedAt(LocalDateTime.now())
                .orderProduct(orderProduct)
                .build();
    }
}
