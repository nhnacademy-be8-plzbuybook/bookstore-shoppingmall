package com.nhnacademy.book.orderProduct.dto;

import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class OrderProductDto {
    private Long orderProductId;
    private String imageUrl;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private BigDecimal price;
    private String status;
    private OrderProductWrapping orderProductWrapping;

    @QueryProjection
    public OrderProductDto(Long orderProductId, String imageUrl, Long bookId, String bookTitle, int quantity, BigDecimal price,
                           OrderProductStatus status, OrderProductWrapping orderProductWrapping) {
        this.orderProductId = orderProductId;
        this.imageUrl = imageUrl;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
        this.status = status.getStatus();
        this.orderProductWrapping = orderProductWrapping;
    }
}

