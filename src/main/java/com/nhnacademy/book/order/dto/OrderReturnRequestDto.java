package com.nhnacademy.book.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderReturnRequestDto {
    @NotBlank
    private String reason;
    private String trackingNumber; // 이거 어떻게?
}
