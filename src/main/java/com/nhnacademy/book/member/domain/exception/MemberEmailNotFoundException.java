package com.nhnacademy.book.member.domain.exception;

public class MemberEmailNotFoundException extends RuntimeException {
    public MemberEmailNotFoundException(String message) {
        super(message);
    }
}
