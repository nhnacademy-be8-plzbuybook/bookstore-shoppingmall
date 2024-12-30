package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class MemberOrderRequestDto extends OrderRequestDto{
    @Setter
    private String memberEmail;
    public MemberOrderRequestDto(@Nullable LocalDate deliveryWishDate, Integer usedPoint,
                                 OrderDeliveryAddress orderDeliveryAddress, OrderProductWrappingDto orderProductWrapping) {
        super(OrderType.MEMBER_ORDER, deliveryWishDate, usedPoint, orderDeliveryAddress, orderProductWrapping);
    }
}
