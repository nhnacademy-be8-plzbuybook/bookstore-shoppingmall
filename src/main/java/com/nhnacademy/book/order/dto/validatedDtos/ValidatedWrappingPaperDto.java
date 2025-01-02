package com.nhnacademy.book.order.dto.validatedDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ValidatedWrappingPaperDto {
    private long id;
    private int quantity;
    private BigDecimal price;
}
