package com.nhnacademy.book.review.exception;

public class InvalidOrderProductStatusException extends RuntimeException {
    public InvalidOrderProductStatusException(String message) {
        super(message);
    }
}
