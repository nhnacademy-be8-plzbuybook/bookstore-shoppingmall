package com.nhnacademy.book.order.enums;

import lombok.Getter;

// 나중에 로케일을 사용한 국제화
public enum OrderStatus {
    PAYMENT_PENDING(0, "결제대기"),
    PAYMENT_COMPLETED(1, "결제완료"),
    PREPARING_FOR_DELIVERY(2, "배송준비중"),
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

    public static String fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status.getStatus();
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + code);
    }
}
