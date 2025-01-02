package com.nhnacademy.book.order.dto.validatedDtos;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidatedOrderProductDto {
    private Long id;
    private int quantity;
    private BigDecimal price;
    private ValidatedWrappingPaperDto validatedWrappingPaperDto;
    private List<ValidateCouponDto> validateCouponDtos;

    public ValidatedOrderProductDto(Long id, int quantity, BigDecimal price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.validateCouponDtos = new ArrayList<>();
    }

    public void applyWrapping(ValidatedWrappingPaperDto validatedWrappingPaperDto) {
        this.validatedWrappingPaperDto = validatedWrappingPaperDto;
    }

    public void applyCoupon(ValidateCouponDto validateCouponDto) {
        this.validateCouponDtos.add(validateCouponDto);
    }
}
