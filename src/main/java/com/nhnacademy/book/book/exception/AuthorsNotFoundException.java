package com.nhnacademy.book.book.exception;

public class AuthorsNotFoundException extends RuntimeException {
    public AuthorsNotFoundException(String message) {
        super(message);
    }
}
