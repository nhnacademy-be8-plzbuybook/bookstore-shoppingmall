package com.nhnacademy.book.cartbook.exception;

public class BookStatusNotSellingBookException extends IllegalArgumentException {
    public BookStatusNotSellingBookException(String message) {
        super(message);
    }
}
