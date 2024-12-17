package com.nhnacademy.book.member.domain.exception;

public class DuplicateAddressException extends RuntimeException {
  public DuplicateAddressException(String message) {
    super(message);
  }
}
