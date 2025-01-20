package com.nhnacademy.book.order.exception;

public class OrderReturnBadRequestException extends RuntimeException {
    public OrderReturnBadRequestException(String message) {
        super(message);
    }
}
