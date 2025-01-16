package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "coupon")
public interface CouponClient {

    @PostMapping("/api/coupons/welcome")
    ResponseEntity<String> issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto);

    @PostMapping("/api/coupons/birthday")
    ResponseEntity<String> issueBirthdayCoupon(@RequestBody BirthdayCouponRequestDto requestDto);

    /**
     * 회원 ID (식별값) 로 회원 쿠폰 조회
     * GET /api/member-coupons/member/{member-id}
     */
    @GetMapping("/api/member-coupons/member/{member-id}")
    ResponseEntity<Page<MemberCouponGetResponseDto>> getMemberCouponsByMemberId(@PathVariable("member-id") Long memberId, Pageable pageable);

    /**
     * 회원이 사용 가능한 쿠폰 목록 조회 (UNUSED 상태의 쿠폰)
     * GET /api/member-coupons/member/{member-id}/unused
     */
    @GetMapping("/api/member-coupons/member/{member-id}/unused")
    ResponseEntity<Page<MemberCouponGetResponseDto>> getUnusedMemberCouponsByMemberId(@PathVariable("member-id") Long memberId, Pageable pageable);


    /**
     * 주문금액 할인계산
     * POST /api/member-coupons/member/{coupon-id}/calculate
     * @param email
     * @param couponId
     * @param calculationRequestDto : BigDecimal price
     * @return
     */
    @PostMapping("/api/member-coupons/member/{coupon-id}/calculate")
    ResponseEntity<CouponCalculationResponseDto> applyOrderProductCoupon(@RequestHeader("X-USER-ID") String email,
                                                                                @PathVariable("coupon-id") Long couponId,
                                                                                @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto);
    @PostMapping("/api/member-coupons/member/{coupon-id}/validation")
    ResponseEntity<ValidationCouponCalculation> validateCouponCalculation(@PathVariable("coupon-id") Long couponId, @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto);

    @GetMapping("/api/coupon-policies/coupon/{coupon-id}")
    ResponseEntity<CouponPolicyResponseDto> findCouponPolicyByCouponId(@PathVariable("coupon-id") @Min(0) Long couponId);

    /**
     * 쿠폰 ID 로 쿠폰 객체 조회
     * GET /api/coupons/id/{coupon-id}
     */
    @GetMapping("/api/coupons/id/{coupon-id}")
    ResponseEntity<CouponResponseDto> getCouponById(@PathVariable("coupon-id") Long couponId);


}
