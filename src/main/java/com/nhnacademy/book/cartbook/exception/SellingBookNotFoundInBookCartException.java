package com.nhnacademy.book.cartbook.exception;

public class SellingBookNotFoundInBookCartException extends RuntimeException {
  public SellingBookNotFoundInBookCartException(String message) {
    super(message);
  }
}
