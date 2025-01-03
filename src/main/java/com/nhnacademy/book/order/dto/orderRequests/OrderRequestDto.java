package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.order.enums.OrderType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class OrderRequestDto {
    private final OrderType orderType;
    @Nullable
    private LocalDate deliveryWishDate;
    @NotNull
    private Integer usedPoint;
    @NotNull
    private List<OrderProductRequestDto> orderProducts;
    @NotNull
    private OrderDeliveryAddressDto orderDeliveryAddress;
    @NotNull
    private BigDecimal deliveryFee;
    @NotNull
    private BigDecimal orderPrice;
}
