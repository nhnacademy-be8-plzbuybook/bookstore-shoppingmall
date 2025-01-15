package com.nhnacademy.book.order.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderReturnSearchRequestDto {
    private String trackingNumber;
    private String status;
}
