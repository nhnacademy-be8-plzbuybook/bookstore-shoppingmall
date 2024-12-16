package com.nhnacademy.book.member.domain.exception;

public class MemberStatusNotFoundException extends RuntimeException {
    public MemberStatusNotFoundException(String message) {
        super(message);
    }
}
