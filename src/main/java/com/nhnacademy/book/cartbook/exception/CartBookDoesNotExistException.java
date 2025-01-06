package com.nhnacademy.book.cartbook.exception;

public class CartBookDoesNotExistException extends RuntimeException {
    public CartBookDoesNotExistException(String message) {
        super(message);
    }
}
