package com.nhnacademy.book.order.dto.orderRequests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderProductWrappingRequestDto {
    @NotNull
    private Long wrappingPaperId;

    @Min(1)
    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;
}
