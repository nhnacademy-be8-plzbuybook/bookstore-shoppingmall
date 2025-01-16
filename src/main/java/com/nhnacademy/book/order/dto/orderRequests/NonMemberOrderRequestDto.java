package com.nhnacademy.book.order.dto.orderRequests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.book.order.enums.OrderType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class NonMemberOrderRequestDto extends OrderRequestDto {
    @NotBlank
    private String nonMemberPassword;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final OrderType orderType = OrderType.NON_MEMBER_ORDER;

    public NonMemberOrderRequestDto(@Nullable LocalDate deliveryWishDate, Integer usedPoint,
                                    OrderDeliveryAddressDto orderDeliveryAddress, List<OrderProductRequestDto> orderProducts,
                                    BigDecimal deliveryFee, BigDecimal orderPrice) {
        super(OrderType.NON_MEMBER_ORDER, deliveryWishDate, usedPoint, orderProducts, orderDeliveryAddress, deliveryFee, orderPrice, null, null);
    }
}
