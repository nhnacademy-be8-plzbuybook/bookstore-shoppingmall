package com.nhnacademy.book.book.exception;

public class AuthorIdNotFoundException extends RuntimeException {
    public AuthorIdNotFoundException(String message) {
        super(message);
    }
}
