package com.nhnacademy.book.member.domain.exception;

public class MemberIdNotFoundException extends RuntimeException {
    public MemberIdNotFoundException(String message) {
        super(message);
    }
}
