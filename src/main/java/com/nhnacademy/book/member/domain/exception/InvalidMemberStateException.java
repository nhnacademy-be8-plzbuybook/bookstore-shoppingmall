package com.nhnacademy.book.member.domain.exception;

public class InvalidMemberStateException extends RuntimeException {
    public InvalidMemberStateException(String message) {
        super(message);
    }
}
