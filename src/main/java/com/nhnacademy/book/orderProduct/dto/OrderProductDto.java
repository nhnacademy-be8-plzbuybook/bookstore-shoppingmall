package com.nhnacademy.book.orderProduct.dto;

import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
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
}

@NoArgsConstructor
@Getter
class OrderProductWrapping {
    String wrappingName;
    int wrappingQuantity;
    BigDecimal wrappingPrice;
}
