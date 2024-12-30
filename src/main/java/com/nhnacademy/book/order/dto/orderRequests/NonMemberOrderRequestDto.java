package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.order.enums.OrderType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class NonMemberOrderRequestDto extends OrderRequestDto {
    @NotBlank
    private String nonMemberPassword;

    public NonMemberOrderRequestDto(@Nullable LocalDate deliveryWishDate, Integer usedPoint,
                                    OrderDeliveryAddressDto orderDeliveryAddressDto, List<OrderProductRequestDto> orderProducts) {
        super(OrderType.NON_MEMBER_ORDER, deliveryWishDate, usedPoint, orderProducts, orderDeliveryAddressDto);
    }
}
