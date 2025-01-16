package com.nhnacademy.book.feign.exception;

public class CouponException extends RuntimeException {
    public CouponException(String message) {
        super(message);
    }
}
