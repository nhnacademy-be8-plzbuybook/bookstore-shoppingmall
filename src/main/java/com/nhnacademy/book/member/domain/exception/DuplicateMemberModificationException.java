package com.nhnacademy.book.member.domain.exception;

public class DuplicateMemberModificationException extends RuntimeException {
    public DuplicateMemberModificationException(String message) {
        super(message);
    }
}
