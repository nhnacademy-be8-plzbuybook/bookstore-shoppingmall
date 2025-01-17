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

    // 회원 ID (식별값) 로 회원 쿠폰 조회
    public Page<MemberCouponGetResponseDto> getMemberCouponsByMemberId(Long memberId, Pageable pageable) {
        try {
            Page<MemberCouponGetResponseDto> getMemberCouponsByMemberId = couponClient.getMemberCouponsByMemberId(memberId, pageable).getBody();

            if (getMemberCouponsByMemberId == null) {
                throw new CouponException("회원쿠폰 조회 에러");
            }
            return getMemberCouponsByMemberId;
        } catch (FeignException | CouponException e) {
            log.error("getMemberCouponsByMemberId Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 회원이 사용 가능한 쿠폰 목록 조회 (UNUSED 상태의 쿠폰)
    public Page<MemberCouponGetResponseDto> getUnusedMemberCouponsByMemberId(Long memberId, Pageable pageable) {
        try {
            Page<MemberCouponGetResponseDto> getUnusedMemberCouponsByMemberId = couponClient.getUnusedMemberCouponsByMemberId(memberId, pageable).getBody();

            if (getUnusedMemberCouponsByMemberId == null) {
                throw new CouponException("회원쿠폰 조회 에러");
            }
            return getUnusedMemberCouponsByMemberId;
        } catch (FeignException | CouponException e) {
            log.error("getUnusedMemberCouponsByMemberId Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 쿠폰 상태 변경: UNUSED -> USED
    public String useCoupon(Long couponId) {
        try {
            String useCoupon = couponClient.useCoupon(couponId).getBody();

            if (useCoupon == null) {
                throw new CouponException("쿠폰상태 변경 에러");
            }

            return useCoupon;
        } catch (FeignException | CouponException e) {
            log.error("useCoupon Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 회원이 보유한 쿠폰 사용
    public String useMemberCoupon(Long memberId, Long couponId) {
        try {
            String useMemberCoupon = couponClient.useMemberCoupon(memberId, couponId).toString();

            if (useMemberCoupon == null) {
                throw new CouponException("회원쿠폰 사용 에러");
            }

            return useMemberCoupon;
        } catch (FeignException | CouponException e) {
            log.error("useMemberCoupon Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 주문금액 할인계산
    public CouponCalculationResponseDto applyOrderProductCoupon(String email, Long couponId, CouponCalculationRequestDto calculationRequestDto) {
        try {
            CouponCalculationResponseDto applyOrderProductCoupon = couponClient.applyOrderProductCoupon(email, couponId, calculationRequestDto).getBody();

            if (applyOrderProductCoupon == null) {
                throw new CouponException("주문금액 할인계산 에러");
            }

            return applyOrderProductCoupon;
        } catch (FeignException | CouponException e) {
            log.error("applyOrderProductCoupon Feign Exception: {}", e.getMessage());
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


    // 쿠폰 ID 로 쿠폰정책 조회
    public CouponPolicyResponseDto findCouponPolicyByCouponId(Long couponId) {
        try {
            CouponPolicyResponseDto findCouponPolicyByCouponId = couponClient.findCouponPolicyByCouponId(couponId).getBody();

            if (findCouponPolicyByCouponId == null) {
                throw new CouponException("쿠폰정책 조회 에러");
            }

            return findCouponPolicyByCouponId;
        } catch (FeignException | CouponException e) {
            log.error("findCouponPolicyByCouponId Feign Exception: {}", e.getMessage());
            throw new CouponException(e.getMessage());
        }
    }

    // 쿠폰 ID 로 쿠폰 객체 조회
    public CouponResponseDto getCouponById(Long couponId) {
        try {
            CouponResponseDto getCouponById = couponClient.getCouponById(couponId).getBody();

            if (getCouponById == null) {
                throw new CouponException("쿠폰 객체 조회 에러");
            }

            return getCouponById;
        } catch (FeignException | CouponException e) {
            log.error("getCouponById Feign Exception: {}", e.getMessage());
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
