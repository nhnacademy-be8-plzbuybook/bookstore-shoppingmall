package com.nhnacademy.book.order.enums;

import lombok.Getter;

// 나중에 로케일을 사용한 국제화
public enum OrderStatus {
    PAYMENT_PENDING(0, "결제대기"),
    PAYMENT_COMPLETED(1, "결제완료"),
    SHIPPED(2, "발송완료"),
    DELIVERING(3, "배송중"),
    DELIVERED(4, "배송완료"),
    PURCHASE_CONFIRMED(5, "구매확정"),
    RETURNED(6, "반품"),
    ORDER_CANCELLED(7, "주문취소");

    @Getter
    private final int code;
    @Getter
    private final String status;

    OrderStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + code);
    }

    public static OrderStatus fromStatus(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equals(status)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + status);
    }
}
