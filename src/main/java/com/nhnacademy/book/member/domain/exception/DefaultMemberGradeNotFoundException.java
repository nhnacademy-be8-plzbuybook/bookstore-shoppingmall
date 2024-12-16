package com.nhnacademy.book.member.domain.exception;

public class DefaultMemberGradeNotFoundException extends RuntimeException {
    public DefaultMemberGradeNotFoundException(String message) {
        super(message);
    }
}
