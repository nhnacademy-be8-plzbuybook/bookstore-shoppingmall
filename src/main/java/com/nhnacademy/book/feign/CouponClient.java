package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.WelComeCouponRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "coupon")
public interface CouponClient {

    @PostMapping("/api/coupons/welcome")
    void issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto);
}
