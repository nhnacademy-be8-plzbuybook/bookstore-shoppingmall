package com.nhnacademy.book.orderProduct.dto;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
@Getter
public class OrderProductSaveRequestDto {
    @NotNull
    private Long sellingBookId;

    @Min(1)
    @NotNull
    private Integer quantity;

    @Nullable
    private List<Long> appliedCouponIds;

    @Nullable
    private BigDecimal couponDiscount;

    @Nullable
    @Valid
    private OrderProductWrappingDto wrapping;
}


