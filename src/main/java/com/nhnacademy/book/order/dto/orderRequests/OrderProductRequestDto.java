package com.nhnacademy.book.order.dto.orderRequests;

import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderProductRequestDto {
    @NotNull
    private Long sellingBookId;

    @Min(1)
    @NotNull
    private Integer quantity;

    @Nullable
    private List<Long> appliedCouponIds;

    @Nullable
    private OrderProductWrappingDto wrapping;
}
