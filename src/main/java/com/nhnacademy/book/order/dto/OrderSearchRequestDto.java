package com.nhnacademy.book.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nhnacademy.book.order.enums.OrderStatus;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class OrderSearchRequestDto {

    @Nullable
    private String memberId;
    @Nullable
    private String productName;
    @Nullable
    private LocalDate orderDate;
    @Nullable
    private OrderStatus orderStatus;
    @Nullable
    private String orderNumber;

    @JsonCreator
    public OrderSearchRequestDto(@Nullable String memberId,
                                 @Nullable String productName,
                                 @Nullable LocalDate orderDate,
                                 @Nullable OrderStatus orderStatus,
                                 @Nullable String orderNumber) {
        this.memberId = memberId;
        this.productName = productName;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderNumber = orderNumber;
    }
}
