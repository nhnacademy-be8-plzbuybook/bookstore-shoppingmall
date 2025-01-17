package com.nhnacademy.book.coupon.service;

import com.nhnacademy.book.coupon.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {

    String issueWelcomeCoupon(WelComeCouponRequestDto requestDto);

    String issueBirthdayCoupon(BirthdayCouponRequestDto requestDto);

    Page<MemberCouponGetResponseDto> getMemberCouponsByMemberId(Long memberId, Pageable pageable);

    Page<MemberCouponGetResponseDto> getUnusedMemberCouponsByMemberId(Long memberId, Pageable pageable);

    String useCoupon(Long couponId);

    String useMemberCoupon(Long memberId, Long couponId);

    CouponCalculationResponseDto applyOrderProductCoupon(String email, Long couponId, CouponCalculationRequestDto calculationRequestDto);

    ValidationCouponCalculation validateCouponCalculation(Long couponId, CouponCalculationRequestDto calculationRequestDto);

    CouponPolicyResponseDto findCouponPolicyByCouponId(Long couponId);

    CouponResponseDto getCouponById(Long couponId);

    String refundCoupon(RefundCouponRequestDto refundCouponRequestDto);
}
