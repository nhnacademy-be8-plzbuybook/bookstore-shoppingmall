package com.nhnacademy.book.handler;

import com.nhnacademy.book.book.exception.*;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.feign.exception.WelcomeCouponIssueException;
import com.nhnacademy.book.member.domain.dto.ErrorResponseDto;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.order.exception.NonMemberPasswordNotMatchException;
import com.nhnacademy.book.order.exception.PriceMismatchException;
import com.nhnacademy.book.review.exception.DuplicateReviewException;
import com.nhnacademy.book.review.exception.InvalidOrderAccessException;
import com.nhnacademy.book.review.exception.InvalidOrderProductStatusException;
import com.nhnacademy.book.review.exception.OrderProductNotFoundException;
import com.nhnacademy.book.skm.exception.KeyMangerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.View;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //중복 이메일
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateEmailException(DuplicateEmailException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //회원 등급을 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberGradeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMemberGradeNotFoundException(MemberGradeNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //회원 상태를 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberStatusNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMemberStatusNotFoundException(MemberStatusNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //기본 등급을 찾을 수 없는 예외 처리
    @ExceptionHandler(DefaultMemberGradeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDefaultMemberGradeNotFoundException(DefaultMemberGradeNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //기본 상태를 찾을 수 없는 예외 처리
    @ExceptionHandler(DefaultStatusGradeNotfoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDefaultStatusGradeNotFoundException(DefaultStatusGradeNotfoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //이메일로 특정 회원을 조회 할 때 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberEmailNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMemberEmailNotFoundException(MemberEmailNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //id로 특정 회원을 조회 할 떄 찾을 수 없는 예외 처리
    @ExceptionHandler(MemberIdNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMemberIdNotFoundException(MemberIdNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //상태를 추가할 때 중복
    @ExceptionHandler(DuplicateMemberStateException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateMemberStateException(DuplicateMemberStateException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //등급을 추가할 때 중복
    @ExceptionHandler(DuplicateMemberGradeException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateMemberGradeException(DuplicateMemberGradeException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //회원을 수정할 떄 기존 값과 같을 경우
    @ExceptionHandler(DuplicateMemberModificationException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateMemberModificationException(DuplicateMemberModificationException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //전체 회원을 조회할 때 등록된 회원이 없는 경우
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMemberNotFoundException(MemberNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //배송지 등록 할때 10개 초과 오류
    @ExceptionHandler(AddressLimitExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleAddressLimitExceededException(AddressLimitExceededException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    //권한이 없는 경우
    @ExceptionHandler(AuthNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthNotFoundException(AuthNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //같은 배송지를 등록 하는 경우
    @ExceptionHandler(DuplicateAddressException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateAddressException(DuplicateAddressException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflictException(ConflictException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    @ExceptionHandler(StockNotEnoughException.class)
    public ResponseEntity<ErrorResponseDto> handleStockNotEnoughException(StockNotEnoughException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    @ExceptionHandler(PriceMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handlePriceMismatchException(PriceMismatchException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    // @RequestBody validation check
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidArgumentException(MethodArgumentNotValidException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    // @RequestPart validation check
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingPartException(MissingServletRequestPartException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    //skm 예외
    @ExceptionHandler(KeyMangerException.class)
    public ResponseEntity<ErrorResponseDto> KeyManagerException(KeyMangerException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }


    @ExceptionHandler(PointConditionNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePointConditionNotFoundException(PointConditionNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }


    //OrderProductId로 OrderProduct를 조회할 때 예외
    @ExceptionHandler(OrderProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleOrderProductNotFoundException(OrderProductNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //회원의 인증 정보가 없을 때 예외
    @ExceptionHandler(CertificationNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCertificationNotFoundException(CertificationNotFoundException e) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //허용되지 않은 인증 방법으로 변경하려할 때
    @ExceptionHandler(InvalidCertificationMethodException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCertificationMethodException(InvalidCertificationMethodException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    //중복된 인증 방법을 추가하려할 때
    @ExceptionHandler(DuplicateCertificationException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateCertificationException(DuplicateCertificationException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //회원가입시 발행하는 Welcome 쿠폰 요청이 실패했을 때
    @ExceptionHandler(WelcomeCouponIssueException.class)
    public ResponseEntity<ErrorResponseDto> handleWelcomeCouponIssueException(WelcomeCouponIssueException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }


    // 여기서부터 Book 관련
    @ExceptionHandler(AuthorIdNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorIdNotFoundException(AuthorIdNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(AuthorNameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorNameNotFoundException(AuthorNameNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(AuthorsNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorsNotFoundException(AuthorsNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(BookAuthorNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleBookAuthorNotFoundException(BookAuthorNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleBookNotFoundException(BookNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //회원 가입시 권한이 없을 시 오류
    @ExceptionHandler(DefaultAuthNotfoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDefaultAuthNotfoundException(DefaultAuthNotfoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(PublisherNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePublisherNotFoundException(PublisherNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(SellingBookNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleSellingBookNotFoundException(SellingBookNotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    //전화번호 중복
    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicatePhoneException(DuplicatePhoneException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //중복리뷰 방지
    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateReviewException(DuplicateReviewException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //상태가 구매 확정인것만 리뷰 작성 가능
    @ExceptionHandler(InvalidOrderProductStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidOrderProductStatusException(InvalidOrderProductStatusException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    //해당 주문이 회원의 주문이 아닐때
    @ExceptionHandler(InvalidOrderAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidOrderAccessException(InvalidOrderAccessException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
    }

    // 비회원주문 비밀번호 틀렸을 때
    @ExceptionHandler(NonMemberPasswordNotMatchException.class)
    public ResponseEntity<ErrorResponseDto> handleNonMemberPasswordNotMatch(NonMemberPasswordNotMatchException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
    }


}
