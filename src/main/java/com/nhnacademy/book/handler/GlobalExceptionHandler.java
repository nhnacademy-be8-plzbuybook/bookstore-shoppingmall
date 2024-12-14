package com.nhnacademy.book.handler;

import com.nhnacademy.book.member.domain.exception.*;
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

    @ExceptionHandler(DefaultMemberGradeNotFoundException.class)
    public ResponseEntity<String> handleDefaultMemberGradeNotFoundException(DefaultMemberGradeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DefaultStatusGradeNotfoundException.class)
    public ResponseEntity<String> handleDefaultStatusGradeNotFoundException(DefaultStatusGradeNotfoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MemberEmailNotFoundException.class)
    public ResponseEntity<String> handleMemberEmailNotFoundException(MemberEmailNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
