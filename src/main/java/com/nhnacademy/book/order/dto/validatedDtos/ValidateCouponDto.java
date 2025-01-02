package com.nhnacademy.book.order.dto.validatedDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ValidateCouponDto {
    private long id;
    private BigDecimal discount;
}
