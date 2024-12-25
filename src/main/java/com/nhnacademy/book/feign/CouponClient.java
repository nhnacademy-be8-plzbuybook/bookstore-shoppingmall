package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.WelComeCouponRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "bookstore-coupon-dev")
public interface CouponClient {

    // Welcome 쿠폰 발급 요청 -> 쿠폰서버의 컨트롤러 호출
    @PostMapping("/api/coupons/welcome-coupon")
    void issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto);
}
