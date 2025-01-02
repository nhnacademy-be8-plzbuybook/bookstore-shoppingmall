package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.BirthdayCouponRequestDto;
import com.nhnacademy.book.feign.dto.MemberCouponResponseDto;
import com.nhnacademy.book.feign.dto.WelComeCouponRequestDto;
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

    @PostMapping("/coupons/birthday")
    ResponseEntity<String> issueBirthdayCoupon(@RequestBody BirthdayCouponRequestDto requestDto);

    // 회원 고유 ID 로 회원이 보유한 쿠폰 목록 조회
    @GetMapping("/api/coupons/member-coupons/member/{memberId}")
    ResponseEntity<Page<MemberCouponResponseDto>> getMemberCouponsByMemberId(
            @PathVariable Long memberId,
            @RequestParam int page,
            @RequestParam int size
    );

    // 회원 고유 ID 로 회원 본인이 사용가능한 쿠폰 목록 조회
    @GetMapping("/member-coupons/member/{memberId}/unused")
    ResponseEntity<Page<MemberCouponResponseDto>> getUnusedMemberCouponsByMemberId(@PathVariable("memberId") Long memberId, Pageable pageable);
}
