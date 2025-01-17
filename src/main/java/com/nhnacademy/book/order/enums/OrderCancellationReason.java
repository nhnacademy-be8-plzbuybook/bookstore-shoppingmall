package com.nhnacademy.book.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderCancellationReason {
    CHANGE_OF_MIND(0, "단순 변심 (상품이 필요 없어짐)"),
    ORDER_MISTAKE(1, "주문 실수 (상품 또는 수량 잘못 선택, 추가 재주문)"),
    PAYMENT_METHOD_CHANGE(2, "다른 결제 수단으로 변경"),
    OTHER(3, "기타");

    private final int code;
    private final String description;
}