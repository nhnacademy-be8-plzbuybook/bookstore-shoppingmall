package com.nhnacademy.book.orderProduct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderProductCancelRequestDto {
    private String cancelReason;
    private String cancelAmount;
}
