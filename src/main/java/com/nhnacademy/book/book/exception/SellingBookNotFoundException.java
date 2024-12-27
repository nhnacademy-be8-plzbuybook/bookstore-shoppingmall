package com.nhnacademy.book.book.exception;

/**
 * Custom exception to be thrown when a SellingBook is not found.
 */
public class SellingBookNotFoundException extends RuntimeException {
    public SellingBookNotFoundException(String message) {
        super(message);
    }
}
