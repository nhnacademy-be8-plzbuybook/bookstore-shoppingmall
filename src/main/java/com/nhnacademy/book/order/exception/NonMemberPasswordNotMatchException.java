package com.nhnacademy.book.order.exception;

public class NonMemberPasswordNotMatchException extends RuntimeException {
    public NonMemberPasswordNotMatchException(String message) {
        super(message);
    }
}
