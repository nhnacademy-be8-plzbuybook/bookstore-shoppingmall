package com.nhnacademy.book.order.dto;

import lombok.Getter;


@Getter
public class MemberOrderSaveRequestDto extends CustomerOrderSaveRequestDto{
    private String memberEmail;
    private String orderId;

    public MemberOrderSaveRequestDto(String memberEmail, String orderId) {
        this.memberEmail = memberEmail;
        this.orderId = orderId;
    }
}
