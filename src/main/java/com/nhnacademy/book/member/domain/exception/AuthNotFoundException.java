package com.nhnacademy.book.member.domain.exception;

public class AuthNotFoundException extends RuntimeException {
    public AuthNotFoundException(String message) {
        super(message);
    }
}
