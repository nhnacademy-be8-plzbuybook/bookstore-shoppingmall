package com.nhnacademy.book.feign.exception;

public class WelcomeCouponIssueException extends RuntimeException {
    public WelcomeCouponIssueException(String message) {
        super(message);
    }
}
