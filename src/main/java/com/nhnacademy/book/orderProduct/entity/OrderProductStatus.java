package com.nhnacademy.book.orderProduct.entity;

import lombok.Getter;

// 나중에 로케일을 사용한 국제화
@Getter
public enum OrderProductStatus {
    PAYMENT_PENDING(0, 3, "결제대기"),
    PAYMENT_COMPLETED(1, 4, "결제완료"),
    SHIPPED(2, 5, "발송완료"),
    DELIVERING(3, 6, "배송중"),
    DELIVERED(4, 7, "배송완료"),
    PURCHASE_CONFIRMED(5, 8, "구매확정"),
    RETURN_REQUESTED(6, 1, "반품요청"),
    ORDER_CANCELLED(7, 0, "주문취소"),
    RETURN_COMPLETED(8, 2, "반품완료");

    private final int code;
    private final int weight;
    private final String status;

    OrderProductStatus(int code, int weight, String status) {
        this.code = code;
        this.weight = weight;
        this.status = status;
    }

    public static OrderProductStatus fromCode(int code) {
        for (OrderProductStatus status : OrderProductStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + code);
    }

    public static OrderProductStatus fromStatus(String status) {
        for (OrderProductStatus orderProductStatus : OrderProductStatus.values()) {
            if (orderProductStatus.getStatus().equals(status)) {
                return orderProductStatus;
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + status);
    }
}
