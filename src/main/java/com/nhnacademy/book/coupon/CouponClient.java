package com.nhnacademy.book.coupon;

import com.nhnacademy.book.coupon.dto.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "coupon")
public interface CouponClient {

    @PostMapping("/api/coupons/welcome")
    ResponseEntity<String> issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto);

    @PostMapping("/api/coupons/birthday")
    ResponseEntity<String> issueBirthdayCoupon(@RequestBody BirthdayCouponRequestDto requestDto);

    @PostMapping("/api/member-coupons/member/{coupon-id}/validation")
    ResponseEntity<ValidationCouponCalculationResponseDto> validateCouponCalculation(@PathVariable("coupon-id") Long couponId, @RequestBody @Valid ValidationCouponCalculationRequestDto calculationRequestDto);

    @PostMapping("/api/coupons/refund")
    ResponseEntity<String> refundCoupon(@RequestBody RefundCouponRequestDto refundCouponRequestDto);

}
