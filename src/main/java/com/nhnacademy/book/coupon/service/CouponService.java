package com.nhnacademy.book.coupon.service;

import com.nhnacademy.book.coupon.dto.*;

public interface CouponService {

    String issueWelcomeCoupon(WelComeCouponRequestDto requestDto);

    String issueBirthdayCoupon(BirthdayCouponRequestDto requestDto);

    ValidationCouponCalculationResponseDto validateCouponCalculation(Long couponId, ValidationCouponCalculationRequestDto validationCouponCalculationRequestDto);

    String refundCoupon(RefundCouponRequestDto refundCouponRequestDto);
}
