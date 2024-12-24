package com.nhnacademy.book.review.exception;

public class OrderProductNotFoundException extends RuntimeException {
    public OrderProductNotFoundException(String message) {
        super(message);
    }
}
