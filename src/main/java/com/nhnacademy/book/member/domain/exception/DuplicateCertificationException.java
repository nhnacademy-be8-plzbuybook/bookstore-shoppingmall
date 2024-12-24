package com.nhnacademy.book.member.domain.exception;

public class DuplicateCertificationException extends RuntimeException {
    public DuplicateCertificationException(String message) {
        super(message);
    }
}
