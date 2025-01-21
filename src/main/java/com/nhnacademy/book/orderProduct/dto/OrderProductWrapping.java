package com.nhnacademy.book.orderProduct.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class OrderProductWrapping { //TODO: 이거 이름 바꿔야됨
    String name;
    int quantity;
    BigDecimal price;

    @QueryProjection
    public OrderProductWrapping(String name, int quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

