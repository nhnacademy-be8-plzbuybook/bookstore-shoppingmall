package com.nhnacademy.book.handler;

import com.nhnacademy.book.member.domain.exception.DuplicateEmailException;
import com.nhnacademy.book.member.domain.exception.MemberGradeNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberStatusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MemberGradeNotFoundException.class)
    public ResponseEntity<String> handleMemberGradeNotFoundException(MemberGradeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MemberStatusNotFoundException.class)
    public ResponseEntity<String> handleMemberStatusNotFoundException(MemberStatusNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
