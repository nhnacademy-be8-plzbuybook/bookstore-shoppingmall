package com.nhnacademy.book.order.dto.validatedDtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class ValidatedOrderDto {
    private List<ValidatedOrderProductDto> orderProducts;
    private int usedPoint;
    private BigDecimal deliveryFee;
    private LocalDate deliveryWishDate;
    private BigDecimal paymentPrice;

    public ValidatedOrderDto(List<ValidatedOrderProductDto> orderProducts, LocalDate deliveryWishDate,
                             int usedPoint, BigDecimal deliveryFee, BigDecimal paymentPrice) {

        this.orderProducts = orderProducts;
        this.deliveryWishDate = deliveryWishDate;
        this.usedPoint = usedPoint;
        this.deliveryFee = deliveryFee;
        this.paymentPrice = paymentPrice;
    }
}
