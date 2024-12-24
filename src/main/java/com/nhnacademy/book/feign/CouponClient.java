package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.WelComeCouponRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "bookstore-coupon-dev")
public interface CouponClient {

    @PostMapping("/api/coupons/welcome-coupon")
    void issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto);
}
