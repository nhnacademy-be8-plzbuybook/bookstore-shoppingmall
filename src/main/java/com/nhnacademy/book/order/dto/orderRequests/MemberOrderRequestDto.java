package com.nhnacademy.book.order.dto.orderRequests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.book.order.enums.OrderType;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class MemberOrderRequestDto extends OrderRequestDto {
    @Setter
    private String memberEmail;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final OrderType orderType = OrderType.MEMBER_ORDER;

    public MemberOrderRequestDto(@Nullable LocalDate deliveryWishDate, Integer usedPoint,
                                 OrderDeliveryAddressDto orderDeliveryAddressDto, List<OrderProductRequestDto> orderProducts,
                                 BigDecimal deliveryFee, BigDecimal orderPrice) {
        super(OrderType.MEMBER_ORDER, deliveryWishDate, usedPoint, orderProducts, orderDeliveryAddressDto, deliveryFee, orderPrice, null, null);
    }
}
