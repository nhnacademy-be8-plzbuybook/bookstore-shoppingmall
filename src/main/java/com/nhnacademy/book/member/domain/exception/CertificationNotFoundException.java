package com.nhnacademy.book.member.domain.exception;

public class CertificationNotFoundException extends RuntimeException {
    public CertificationNotFoundException(String message) {
        super(message);
    }
}
