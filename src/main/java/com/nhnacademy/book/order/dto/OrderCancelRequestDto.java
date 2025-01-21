package com.nhnacademy.book.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class OrderCancelRequestDto {
    @NotBlank
    private String reason;
    @NotNull
    private List<OrderProductCancelRequestDto> cancelProducts;
}
