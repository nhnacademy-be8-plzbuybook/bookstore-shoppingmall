package com.nhnacademy.book.order.exception;

public class OrderCompletionFailException extends RuntimeException {
    public OrderCompletionFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
