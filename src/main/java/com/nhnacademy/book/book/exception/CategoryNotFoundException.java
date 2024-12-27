package com.nhnacademy.book.book.exception;

public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException(String message) {
    super(message);
  }
}
