package com.nhnacademy.book.orderProduct.entity;

import lombok.Getter;

// 나중에 로케일을 사용한 국제화
public enum OrderProductStatus {
    RETURNED(6, "반품"),
    CANCELLED(7, "주문취소");

    @Getter
    private final int code;
    @Getter
    private final String status;

    OrderProductStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public static String fromCode(int code) {
        for (OrderProductStatus status : OrderProductStatus.values()) {
            if (status.getCode() == code) {
                return status.getStatus();
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + code);
    }
}
