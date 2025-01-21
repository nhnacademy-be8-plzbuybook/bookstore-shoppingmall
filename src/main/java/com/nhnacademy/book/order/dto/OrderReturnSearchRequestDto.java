package com.nhnacademy.book.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderReturnSearchRequestDto {
    private String trackingNumber;
    private String status;
}
