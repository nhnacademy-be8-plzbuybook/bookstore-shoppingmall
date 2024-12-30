package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.BirthdayCouponRequestDto;
import com.nhnacademy.book.feign.dto.MemberCouponResponseDto;
import com.nhnacademy.book.feign.dto.WelComeCouponRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "coupon")
public interface CouponClient {

    @PostMapping("/api/coupons/welcome")
    ResponseEntity<String> issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto);

    @PostMapping("/coupons/birthday")
    ResponseEntity<String> issueBirthdayCoupon(@RequestBody BirthdayCouponRequestDto requestDto);

    @GetMapping("/api/coupons/member-coupon/member/{memberId}")
    ResponseEntity<Page<MemberCouponResponseDto>> getMemberCouponsByMemberId(
            @PathVariable Long memberId,
            @RequestParam int page,
            @RequestParam int size
    );
}
