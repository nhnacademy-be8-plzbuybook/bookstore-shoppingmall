package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class OrderRequestDto {
    @NotNull
    private OrderType orderType;
    @Nullable
    private LocalDate deliveryWishDate;
    @NotNull
    private Integer usedPoint;
    @NotNull
    private OrderDeliveryAddress orderDeliveryAddress;
    @NotNull
    private OrderProductWrappingDto orderProductWrapping;
}
