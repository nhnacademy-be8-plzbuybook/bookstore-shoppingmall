package com.nhnacademy.book.member.domain.exception;

public class DuplicateMemberStateException extends RuntimeException {
    public DuplicateMemberStateException(String message) {
        super(message);
    }
}
