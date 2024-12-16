package com.nhnacademy.book.book.exception;

public class AuthorNameNotFoundException extends RuntimeException {
    public AuthorNameNotFoundException(String message) {
        super(message);
    }
}
