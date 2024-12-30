package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NonMemberOrderRequestDto extends OrderRequestDto{
    private String nonMemberPassword;
    public NonMemberOrderRequestDto(@Nullable LocalDate deliveryWishDate, Integer usedPoint,
                                    OrderDeliveryAddress orderDeliveryAddress, OrderProductWrappingDto orderProductWrapping) {
        super(OrderType.NON_MEMBER_ORDER, deliveryWishDate, usedPoint, orderDeliveryAddress, orderProductWrapping);
    }
}
