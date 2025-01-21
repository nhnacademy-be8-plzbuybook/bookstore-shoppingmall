package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
@AllArgsConstructor
@Getter
public class OrderProductRequestDto {
    @NotNull
    private Long productId;

    @NotNull
    private BigDecimal price;

    @Min(1)
    @NotNull
    private Integer quantity;

    @Nullable
    private List<OrderProductAppliedCouponDto> appliedCoupons;

    @Nullable
    private OrderProductWrappingDto wrapping;
}
