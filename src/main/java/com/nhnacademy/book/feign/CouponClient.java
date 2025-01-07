package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.BirthdayCouponRequestDto;
import com.nhnacademy.book.feign.dto.MemberCouponGetResponseDto;
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
}
