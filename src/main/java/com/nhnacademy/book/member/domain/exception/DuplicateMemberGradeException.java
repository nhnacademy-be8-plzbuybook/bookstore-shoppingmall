package com.nhnacademy.book.member.domain.exception;

public class DuplicateMemberGradeException extends RuntimeException {
    public DuplicateMemberGradeException(String message) {
        super(message);
    }
}
