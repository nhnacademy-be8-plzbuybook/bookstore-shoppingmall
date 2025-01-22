package com.nhnacademy.book.order.exception;

public class OrderCacheException extends RuntimeException{
    public OrderCacheException(String message) {
        super(message);
    }

    public OrderCacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
