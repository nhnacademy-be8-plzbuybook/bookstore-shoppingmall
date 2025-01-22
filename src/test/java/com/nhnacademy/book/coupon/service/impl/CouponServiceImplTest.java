package com.nhnacademy.book.coupon.service.impl;

import com.nhnacademy.book.coupon.CouponClient;
import com.nhnacademy.book.coupon.dto.*;
import com.nhnacademy.book.coupon.exception.CouponException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {
    @Mock
    private CouponClient couponClient;

    private CouponServiceImpl couponService;

    private WelComeCouponRequestDto welcomeCouponRequestDto;
    private BirthdayCouponRequestDto birthdayCouponRequestDto;
    private ValidationCouponCalculationRequestDto validationRequestDto;
    private RefundCouponRequestDto refundCouponRequestDto;

    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        couponService = new CouponServiceImpl(couponClient);

        welcomeCouponRequestDto = new WelComeCouponRequestDto(1L, now);
        birthdayCouponRequestDto = new BirthdayCouponRequestDto(2L, now);
        validationRequestDto = new ValidationCouponCalculationRequestDto(new BigDecimal("1000"));
        refundCouponRequestDto = new RefundCouponRequestDto(1L, 1L);
    }

    @DisplayName("웰컴 쿠폰발급")
    @Test
    void issueWelcomeCoupon() {
        when(couponClient.issueWelcomeCoupon(any(WelComeCouponRequestDto.class)))
                .thenReturn(ResponseEntity.ok("웰컴 쿠폰이 성공적으로 발급되었습니다"));

        String response = couponService.issueWelcomeCoupon(welcomeCouponRequestDto);

        assertEquals("웰컴 쿠폰이 성공적으로 발급되었습니다", response);
        verify(couponClient, times(1)).issueWelcomeCoupon(any(WelComeCouponRequestDto.class));
    }

    @DisplayName("웰컴 쿠폰발급 실패")
    @Test
    void issueWelcomeCoupon_CouponException() {
        when(couponClient.issueWelcomeCoupon(any(WelComeCouponRequestDto.class))).thenReturn(ResponseEntity.ok(null));

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.issueWelcomeCoupon(welcomeCouponRequestDto)
        );

        assertTrue(exception.getMessage().contains("회원가입쿠폰 발급 에러"));
    }

    @DisplayName("생일 쿠폰발급")
    @Test
    void issueBirthdayCoupon() {
        when(couponClient.issueBirthdayCoupon(any(BirthdayCouponRequestDto.class)))
                .thenReturn(ResponseEntity.ok("생일 쿠폰이 성공적으로 발급되었습니다"));

        String response = couponService.issueBirthdayCoupon(birthdayCouponRequestDto);

        assertEquals("생일 쿠폰이 성공적으로 발급되었습니다", response);
        verify(couponClient, times(1)).issueBirthdayCoupon(any(BirthdayCouponRequestDto.class));
    }

    @DisplayName("생일 쿠폰발급 실패")
    @Test
    void issueBirthdayCoupon_CouponException() {
        when(couponClient.issueBirthdayCoupon(any(BirthdayCouponRequestDto.class))).thenReturn(ResponseEntity.ok(null));

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.issueBirthdayCoupon(birthdayCouponRequestDto)
        );

        assertTrue(exception.getMessage().contains("생일쿠폰 발급 에러"));
    }

    @DisplayName("할인계산 검증")
    @Test
    void validateCouponCalculation() {
        ValidationCouponCalculationResponseDto mockResponse = new ValidationCouponCalculationResponseDto(new BigDecimal("1000")
        );

        when(couponClient.validateCouponCalculation(eq(1L), any(ValidationCouponCalculationRequestDto.class))).thenReturn(ResponseEntity.ok(mockResponse));

        ValidationCouponCalculationResponseDto response = couponService.validateCouponCalculation(1L, validationRequestDto);

        assertEquals(mockResponse.price(), response.price());
        verify(couponClient, times(1)).validateCouponCalculation(eq(1L), any(ValidationCouponCalculationRequestDto.class));
    }

    @DisplayName("할인계산 검증 실패")
    @Test
    void validateCouponCalculation_CouponException() {
        when(couponClient.validateCouponCalculation(eq(1L), any(ValidationCouponCalculationRequestDto.class))).thenReturn(ResponseEntity.ok(null));

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.validateCouponCalculation(1L, validationRequestDto)
        );

        assertTrue(exception.getMessage().contains("주문금액 할인계산 검증 에러"));
    }

    @DisplayName("쿠폰 환불")
    @Test
    void refundCoupon() {
        when(couponClient.refundCoupon(any(RefundCouponRequestDto.class))).thenReturn(ResponseEntity.ok("환불이 완료되었습니다"));

        String response = couponService.refundCoupon(refundCouponRequestDto);

        assertEquals("환불이 완료되었습니다", response);
        verify(couponClient, times(1)).refundCoupon(any(RefundCouponRequestDto.class));
    }

    @DisplayName("쿠폰 환불 실패")
    @Test
    void refundCoupon_CouponException() {
        when(couponClient.refundCoupon(any(RefundCouponRequestDto.class))).thenReturn(ResponseEntity.ok(null));

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.refundCoupon(refundCouponRequestDto)
        );

        assertTrue(exception.getMessage().contains("쿠폰환불 에러"));
    }

    @DisplayName("쿠폰 사용")
    @Test
    void useCoupon() {
        Long couponId = 1L;
        when(couponClient.useCoupon(couponId)).thenReturn(ResponseEntity.ok("쿠폰 상태가 변경되었습니다"));

        String response = couponService.useCoupon(couponId);

        assertEquals("쿠폰 상태가 변경되었습니다", response);
        verify(couponClient, times(1)).useCoupon(couponId);
    }

    @DisplayName("쿠폰 사용 실패")
    @Test
    void useCoupon_CouponException() {
        Long couponId = 1L;
        when(couponClient.useCoupon(couponId)).thenReturn(ResponseEntity.ok(null));

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.useCoupon(couponId)
        );

        assertTrue(exception.getMessage().contains("쿠폰사용 에러"));
    }

    @DisplayName("쿠폰 사용 취소")
    @Test
    void cancelCoupon() {
        Long couponId = 1L;
        when(couponClient.cancelCoupon(couponId)).thenReturn(ResponseEntity.ok("쿠폰 사용이 취소되었습니다"));

        String response = couponService.cancelCoupon(couponId);

        assertEquals("쿠폰 사용이 취소되었습니다", response);
        verify(couponClient, times(1)).cancelCoupon(couponId);
    }

    @DisplayName("쿠폰 사용 취소 실패")
    @Test
    void cancelCoupon_CouponException() {
        Long couponId = 1L;
        when(couponClient.cancelCoupon(couponId)).thenReturn(ResponseEntity.ok(null));

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.cancelCoupon(couponId)
        );

        assertTrue(exception.getMessage().contains("쿠폰사용 취소 에러"));
    }
}