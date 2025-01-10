package com.nhnacademy.book.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NonMemberOrderAccessResponseDto {
    private String orderId;
    private String password;

    @QueryProjection
    public NonMemberOrderAccessResponseDto(String orderId, String password) {
        this.orderId = orderId;
        this.password = password;
    }
}
