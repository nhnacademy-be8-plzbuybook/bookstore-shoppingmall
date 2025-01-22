package com.nhnacademy.book.objectstorage.exception;

public class ObjectStorageFileUploadException extends RuntimeException {
    public ObjectStorageFileUploadException(String message) {
        super(message);
    }
    public ObjectStorageFileUploadException(String message, Throwable cause) {
        super(message, cause); // 원인 예외를 포함
    }
}
