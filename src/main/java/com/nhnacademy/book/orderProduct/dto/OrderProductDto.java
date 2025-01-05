package com.nhnacademy.book.orderProduct.dto;

import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class OrderProductDto {
    private String imageUrl;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private BigDecimal price;
    private OrderProductStatus status;
    private OrderProductWrapping orderProductWrapping;

    @QueryProjection
    public OrderProductDto(String imageUrl, Long bookId, String bookTitle, int quantity, BigDecimal price,
                           OrderProductStatus status, OrderProductWrapping orderProductWrapping) {
        this.imageUrl = imageUrl;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.orderProductWrapping = orderProductWrapping;
    }
}

