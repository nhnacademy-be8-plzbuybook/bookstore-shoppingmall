package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.OrderProductCancel;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderProductCancelRequestDto {
    @NotBlank
    private String reason;

    @NotNull
    @Min(1)
    private int quantity;

    @NotNull
    private BigDecimal price;

    public OrderProductCancel toEntity(OrderProduct orderProduct) {
        return OrderProductCancel.builder()
                .reason(reason)
                .quantity(quantity)
                .canceledAt(LocalDateTime.now())
                .orderProduct(orderProduct)
                .build();
    }
}
