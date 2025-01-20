package com.nhnacademy.book.orderProduct.entity;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import lombok.Getter;

// 나중에 로케일을 사용한 국제화
@Getter
public enum OrderProductStatus {
    PAYMENT_PENDING(0, "결제대기"),
    PAYMENT_COMPLETED(1, "결제완료"),
    SHIPPED(2, "발송완료"),
    DELIVERING(3, "배송중"),
    DELIVERED(4, "배송완료"),
    PURCHASE_CONFIRMED(5, "구매확정"),
    RETURN_REQUESTED(6, "반품요청"),
    ORDER_CANCELLED(7, "주문취소"),
    RETURN_COMPLETED(8, "반품완료");

    private final int code;
    private final String status;

    OrderProductStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public static OrderProductStatus fromCode(int code) {
        for (OrderProductStatus status : OrderProductStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new NotFoundException("주문상태를 찾을 수 없습니다. 코드: " + code);
    }

    public static OrderProductStatus fromStatus(String status) {
        for (OrderProductStatus orderProductStatus : OrderProductStatus.values()) {
            if (orderProductStatus.getStatus().equals(status)) {
                return orderProductStatus;
            }
        }
        throw new NotFoundException("주문상태를 찾을 수 없습니다. 주문상태: " + status);
    }
}
