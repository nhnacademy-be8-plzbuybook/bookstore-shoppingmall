package com.nhnacademy.book.book.exception;

public class BookAuthorNotFoundException extends RuntimeException {
    public BookAuthorNotFoundException(String message) {
        super(message);
    }
}
