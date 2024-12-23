package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.orderProduct.dto.OrderProductSaveRequestDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class OrderSaveRequestDto {
    @NotBlank
    private String orderId;

    @NotBlank
    private String orderName;

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


    public Orders toOrderEntity() {
        return Orders.builder()
                .id(orderId)
                .name(orderName)
                .totalPrice(totalPrice)
                .deliveryWishDate(deliveryWishDate)
                .usedPoint(usedPoint)
                .build();
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
