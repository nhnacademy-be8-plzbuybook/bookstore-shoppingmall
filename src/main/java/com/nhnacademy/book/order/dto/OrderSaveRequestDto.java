package com.nhnacademy.book.order.dto;

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
    private OrderDeliveryAddress orderDeliveryAddress;

    @Builder
    public OrderSaveRequestDto(BigDecimal totalPrice, @Nullable LocalDate deliveryWishDate, Integer usedPoint,
                               List<OrderProductSaveRequestDto> orderProducts, OrderDeliveryAddress orderDeliveryAddress) {
        this.totalPrice = totalPrice;
        this.deliveryWishDate = deliveryWishDate;
        this.usedPoint = usedPoint;
        this.orderProducts = orderProducts;
        this.orderDeliveryAddress = orderDeliveryAddress;
    }
}
@Getter
class OrderDeliveryAddress {
    @NotBlank
    private String locationAddress;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String detailAddress;

    @NotBlank
    private String recipient;

    @NotBlank
    private String recipientPhone;
}
