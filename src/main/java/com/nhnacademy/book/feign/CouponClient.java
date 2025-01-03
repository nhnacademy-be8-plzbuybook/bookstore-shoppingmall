package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.BirthdayCouponRequestDto;
import com.nhnacademy.book.feign.dto.MemberCouponResponseDto;
import com.nhnacademy.book.feign.dto.WelComeCouponRequestDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "coupon")
public interface CouponClient {

    // Welcome 쿠폰
    @PostMapping("/api/coupons/welcome")
    ResponseEntity<String> issueWelcomeCoupon(@RequestBody @Valid WelComeCouponRequestDto requestDto);

    // 생일 쿠폰
    @PostMapping("/api/coupons/birthday")
    ResponseEntity<String> issueBirthdayCoupon(@RequestBody BirthdayCouponRequestDto requestDto);

}
