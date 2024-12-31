package com.nhnacademy.book.order.dto;

import com.nhnacademy.book.order.enums.OrderStatus;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class OrderSearchRequestDto {

    @Setter
    @Nullable
    private String memberId;
    @Nullable
    private String productName;
    @Nullable
    private LocalDate orderDate;
    @Nullable
    private OrderStatus orderStatus;

    public OrderSearchRequestDto(@Nullable String memberId, @Nullable String productName,
                                 @Nullable LocalDate orderDate, @Nullable OrderStatus orderStatus) {
        this.memberId = memberId;
        this.productName = productName;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}
