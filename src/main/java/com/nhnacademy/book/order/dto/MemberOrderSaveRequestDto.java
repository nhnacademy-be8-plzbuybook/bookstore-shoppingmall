package com.nhnacademy.book.order.dto;

import lombok.Getter;


@Getter
public class MemberOrderSaveRequestDto extends CustomerOrderSaveRequestDto{
    private Long memberId; //TODO: 둘중에 하나만 해야됨
    private String memberEmail;
    private String orderId;

    public MemberOrderSaveRequestDto(String memberEmail, String orderId) {
        this.memberEmail = memberEmail;
        this.orderId = orderId;
    }

    public MemberOrderSaveRequestDto(Long memberId, String orderId) {
        this.memberId = memberId;
        this.orderId = orderId;
    }
}
