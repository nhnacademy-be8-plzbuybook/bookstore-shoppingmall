package com.nhnacademy.book.handler;

import com.nhnacademy.book.member.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //중복 이메일
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    //회원 등급을 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberGradeNotFoundException.class)
    public ResponseEntity<String> handleMemberGradeNotFoundException(MemberGradeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //회원 상태를 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberStatusNotFoundException.class)
    public ResponseEntity<String> handleMemberStatusNotFoundException(MemberStatusNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //기본 등급을 찾을 수 없는 예외 처리
    @ExceptionHandler(DefaultMemberGradeNotFoundException.class)
    public ResponseEntity<String> handleDefaultMemberGradeNotFoundException(DefaultMemberGradeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //기본 상태를 찾을 수 없는 예외 처리
    @ExceptionHandler(DefaultStatusGradeNotfoundException.class)
    public ResponseEntity<String> handleDefaultStatusGradeNotFoundException(DefaultStatusGradeNotfoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //이메일로 특정 회원을 조회 할 때 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberEmailNotFoundException.class)
    public ResponseEntity<String> handleMemberEmailNotFoundException(MemberEmailNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //id로 특정 회원을 조회 할 떄 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberIdNotFoundException.class)
    public ResponseEntity<String> handleMemberIdNotFoundException(MemberIdNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //상태를 추가할 때 중복
    @ExceptionHandler(DuplicateMemberStateException.class)
    public ResponseEntity<String> handleDuplicateMemberStateException(DuplicateMemberStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    //등급을 추가할 때 중복
    @ExceptionHandler(DuplicateMemberGradeException.class)
    public ResponseEntity<String> handleDuplicateMemberGradeException(DuplicateMemberGradeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    //회원을 수정할 떄 기존 값과 같을 경우
    @ExceptionHandler(DuplicateMemberModificationException.class)
    public ResponseEntity<String> handleDuplicateMemberModificationException(DuplicateMemberModificationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
