package com.nhnacademy.book.coupon.service.impl;

import com.nhnacademy.book.coupon.CouponClient;
import com.nhnacademy.book.coupon.dto.*;
import com.nhnacademy.book.coupon.exception.CouponException;
import com.nhnacademy.book.coupon.service.CouponService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {
    private final CouponClient couponClient;

    // 회원가입쿠폰 (welcome) 발급
    public String issueWelcomeCoupon(WelComeCouponRequestDto requestDto) {
        try {
            String issueWelcomeCoupon = couponClient.issueWelcomeCoupon(requestDto).getBody();

            if (issueWelcomeCoupon == null) {
                throw new CouponException("회원가입쿠폰 발급 에러");
            }

            return issueWelcomeCoupon;
        } catch (FeignException | CouponException e) {
            log.error("issueWelcomeCoupon Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 생일쿠폰 발급
    public String issueBirthdayCoupon(BirthdayCouponRequestDto requestDto) {
        try {
            String issueBirthdayCoupon = couponClient.issueBirthdayCoupon(requestDto).getBody();

            if (issueBirthdayCoupon == null) {
                throw new CouponException("생일쿠폰 발급 에러");
            }

            return issueBirthdayCoupon;
        } catch (FeignException | CouponException e) {
            log.error("issueBirthdayCoupon Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 주문금액 할인계산 검증
    public ValidationCouponCalculation validateCouponCalculation(Long couponId, CouponCalculationRequestDto calculationRequestDto) {
        try {
            ValidationCouponCalculation validateCouponCalculation = couponClient.validateCouponCalculation(couponId, calculationRequestDto).getBody();

            if (validateCouponCalculation == null) {
                throw new CouponException("주문금액 할인계산 검증 에러");
            }

            return validateCouponCalculation;
        } catch (FeignException | CouponException e) {
            log.error("validateCouponCalculation Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 쿠폰 환불
    public String refundCoupon(RefundCouponRequestDto refundCouponRequestDto) {
        try {
            String refundCoupon = couponClient.refundCoupon(refundCouponRequestDto).getBody();

            if (refundCoupon == null) {
                throw new CouponException("쿠폰환불 에러");
            }

            return refundCoupon;
        } catch (FeignException | CouponException e) {
            log.error("refundCoupon Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }


}
