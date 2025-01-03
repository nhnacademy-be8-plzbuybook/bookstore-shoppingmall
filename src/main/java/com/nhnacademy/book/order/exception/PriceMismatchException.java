package com.nhnacademy.book.order.exception;

public class PriceMismatchException extends RuntimeException{
    public PriceMismatchException(String message) {
        super(message);
    }
}
