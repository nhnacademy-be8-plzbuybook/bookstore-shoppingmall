package com.nhnacademy.book.member.domain.exception;

public class MemberGradeNotFoundException extends RuntimeException {
    public MemberGradeNotFoundException(String message) {
        super(message);
    }
}
