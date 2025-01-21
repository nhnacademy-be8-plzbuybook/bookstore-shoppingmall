package com.nhnacademy.book.coupon.service;

import com.nhnacademy.book.coupon.dto.*;

public interface CouponService {

    String issueWelcomeCoupon(WelComeCouponRequestDto requestDto);

    String issueBirthdayCoupon(BirthdayCouponRequestDto requestDto);

    ValidationCouponCalculation validateCouponCalculation(Long couponId, CouponCalculationRequestDto calculationRequestDto);

    String refundCoupon(RefundCouponRequestDto refundCouponRequestDto);
}
