package com.nhnacademy.book.coupon;

import com.nhnacademy.book.coupon.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponClientTest {

    @Mock
    private CouponClient couponClient;

    private final LocalDateTime now = LocalDateTime.now();

    private WelComeCouponRequestDto welcomeCouponRequestDto;
    private BirthdayCouponRequestDto birthdayCouponRequestDto;
    private ValidationCouponCalculationRequestDto validationRequestDto;
    private RefundCouponRequestDto refundCouponRequestDto;

    @BeforeEach
    void setUp() {
        welcomeCouponRequestDto = new WelComeCouponRequestDto(1L, now);
        birthdayCouponRequestDto = new BirthdayCouponRequestDto(2L, now);
        validationRequestDto = new ValidationCouponCalculationRequestDto(new BigDecimal("1000"));
        refundCouponRequestDto = new RefundCouponRequestDto(1L, 1L);
    }

    @DisplayName("웰컴 쿠폰 발급")
    @Test
    void issueWelcomeCoupon() {
        when(couponClient.issueWelcomeCoupon(any(WelComeCouponRequestDto.class))).thenReturn(ResponseEntity.ok("웰컴 쿠폰이 성공적으로 발급되었습니다"));

        ResponseEntity<String> response = couponClient.issueWelcomeCoupon(welcomeCouponRequestDto);

        assertEquals("웰컴 쿠폰이 성공적으로 발급되었습니다", response.getBody());
        verify(couponClient, times(1)).issueWelcomeCoupon(any());
    }

    @DisplayName("생일 쿠폰 발급")
    @Test
    void issueBirthdayCoupon() {
        when(couponClient.issueBirthdayCoupon(any(BirthdayCouponRequestDto.class))).thenReturn(ResponseEntity.ok("생일 쿠폰이 성공적으로 발급되었습니다"));

        ResponseEntity<String> response = couponClient.issueBirthdayCoupon(birthdayCouponRequestDto);

        assertEquals("생일 쿠폰이 성공적으로 발급되었습니다", response.getBody());
        verify(couponClient, times(1)).issueBirthdayCoupon(any());
    }

    @DisplayName("할인쿠폰 계산검증")
    @Test
    void validateCouponCalculation() {
        ValidationCouponCalculationResponseDto validationResponseDto = new ValidationCouponCalculationResponseDto(new BigDecimal("900"));

        when(couponClient.validateCouponCalculation(eq(1L), any(ValidationCouponCalculationRequestDto.class))).thenReturn(ResponseEntity.ok(validationResponseDto));

        ResponseEntity<ValidationCouponCalculationResponseDto> response = couponClient.validateCouponCalculation(1L, validationRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validationResponseDto, response.getBody());

        verify(couponClient, times(1)).validateCouponCalculation(eq(1L), any(ValidationCouponCalculationRequestDto.class));
    }

    @DisplayName("쿠폰 환불")
    @Test
    void refundCoupon() {
        when(couponClient.refundCoupon(any(RefundCouponRequestDto.class))).thenReturn(ResponseEntity.ok("환불이 완료되었습니다"));

        ResponseEntity<String> response = couponClient.refundCoupon(refundCouponRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("환불이 완료되었습니다", response.getBody());

        verify(couponClient, times(1)).refundCoupon(any(RefundCouponRequestDto.class));
    }
}