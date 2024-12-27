package com.nhnacademy.book.member.domain.exception;

public class AddressLimitExceededException extends RuntimeException {
    public AddressLimitExceededException(String message) {
        super(message);
    }
}
