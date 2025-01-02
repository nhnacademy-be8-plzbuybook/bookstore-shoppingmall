package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.orderProduct.dto.OrderProductSaveRequestDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class OrderSaveRequestDto {
    @NotBlank
    private BigDecimal totalPrice;

    @Nullable
    private LocalDate deliveryWishDate;

    @NotNull
    private Integer usedPoint;

    @NotNull
    private List<OrderProductSaveRequestDto> orderProducts;

    @NotNull
    private OrderDeliveryAddressDto orderDeliveryAddressDto;

    @Builder
    public OrderSaveRequestDto(BigDecimal totalPrice, @Nullable LocalDate deliveryWishDate, Integer usedPoint,
                               List<OrderProductSaveRequestDto> orderProducts, OrderDeliveryAddressDto orderDeliveryAddressDto) {
        this.totalPrice = totalPrice;
        this.deliveryWishDate = deliveryWishDate;
        this.usedPoint = usedPoint;
        this.orderProducts = orderProducts;
        this.orderDeliveryAddressDto = orderDeliveryAddressDto;
    }
}
