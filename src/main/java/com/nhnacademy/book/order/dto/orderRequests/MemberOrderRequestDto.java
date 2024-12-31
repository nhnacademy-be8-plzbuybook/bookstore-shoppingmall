package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.order.enums.OrderType;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MemberOrderRequestDto extends OrderRequestDto{
    @Setter
    private String memberEmail;

    public MemberOrderRequestDto(@Nullable LocalDate deliveryWishDate, Integer usedPoint,
                                 OrderDeliveryAddressDto orderDeliveryAddressDto, List<OrderProductRequestDto> orderProducts) {
        super(OrderType.MEMBER_ORDER, deliveryWishDate, usedPoint, orderProducts, orderDeliveryAddressDto);
    }
}