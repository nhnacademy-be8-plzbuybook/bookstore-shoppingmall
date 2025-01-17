package com.nhnacademy.book.payment.exception;

public class PaymentFailException extends RuntimeException {
    public PaymentFailException(String message) {
        super(message);
    }
}
