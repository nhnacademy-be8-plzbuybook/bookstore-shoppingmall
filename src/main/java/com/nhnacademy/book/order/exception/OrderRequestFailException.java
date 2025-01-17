package com.nhnacademy.book.order.exception;

public class OrderRequestFailException extends RuntimeException {
    public OrderRequestFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
