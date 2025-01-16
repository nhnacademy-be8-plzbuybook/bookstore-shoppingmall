package com.nhnacademy.book.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderReturnReason {
    CHANGE_OF_MIND(0, "단순 변심"),
    PRODUCT_FAULT(1, "상품 불량, 파손"),
    OTHER(2, "기타");

    private final int code;
    private final String description;

}
