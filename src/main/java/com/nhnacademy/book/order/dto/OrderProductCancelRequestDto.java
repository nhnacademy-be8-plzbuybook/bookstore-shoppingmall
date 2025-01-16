package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderProductCancel;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderProductCancelRequestDto {
    @NotNull
    private Long orderProductId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public OrderProductCancel toEntity(String reason, OrderProduct orderProduct) {
        return OrderProductCancel.builder()
                .reason(reason)
                .quantity(quantity)
                .canceledAt(LocalDateTime.now())
                .orderProduct(orderProduct)
                .build();
    }
}
