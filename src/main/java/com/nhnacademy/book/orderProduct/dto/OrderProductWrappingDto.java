package com.nhnacademy.book.orderProduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderProductWrappingDto {
    //wrapping이 null일 수도 있지만 null이 아니면 내부 값들은 조건을 만족해야함
    @NotNull
    private Long wrappingPaperId;

    @Min(1)
    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;
}
