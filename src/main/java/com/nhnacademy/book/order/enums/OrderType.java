package com.nhnacademy.book.order.enums;

public enum OrderType {
    MEMBER_ORDER(1, "회원주문"),
    NON_MEMBER_ORDER(1, "비회원주문");

    private int code;
    private String type;

    OrderType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static String fromCode(int code) {
        for (OrderType orderType: OrderType.values()) {
            if (orderType.code == code) {
                return orderType.type;
            }
        }
        throw new IllegalArgumentException("Invalid order type code: " + code);
    }
}
