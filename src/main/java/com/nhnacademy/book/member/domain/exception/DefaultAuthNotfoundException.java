package com.nhnacademy.book.member.domain.exception;

public class DefaultAuthNotfoundException extends RuntimeException {
    public DefaultAuthNotfoundException(String message) {
        super(message);
    }
}
