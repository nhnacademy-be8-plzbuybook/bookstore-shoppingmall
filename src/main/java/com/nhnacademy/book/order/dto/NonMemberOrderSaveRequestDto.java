package com.nhnacademy.book.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NonMemberOrderSaveRequestDto extends CustomerOrderSaveRequestDto{
    private String orderId;
    private String nonMemberOrderPassword;
}
