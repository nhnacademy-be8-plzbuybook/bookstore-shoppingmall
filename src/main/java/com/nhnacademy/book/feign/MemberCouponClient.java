package com.nhnacademy.book.feign;

import com.nhnacademy.book.feign.dto.MemberCouponGetMemberResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bookstore-coupon-dev", url = "http://localhost:8083")
public interface MemberCouponClient {
    // 쿠폰서버의 MemberCoupon 조회 API 호출
    @GetMapping("/api//member-coupons/{mcMemberId}")
    ResponseEntity<Page<MemberCouponGetMemberResponseDto>> getMemberCoupons(
            @PathVariable("mcMemberId") Long mcMemberId,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );
}
/*
0. 위 페인클라이언트의 목적은 '회원의 쿠폰함 조회' 를 구현하기 위함
1. url 은 유레카인지 쿠폰서버의 url 인지 질문필요함
2. 쿠폰서버는 dto 를 record 로 사용하고 있는데 쇼핑몰서버처럼 dto 를 class 로 통일해야하는지
3. MemberCouponClient 를 주입받아 서비스 로직을 만들고 컨트롤러로 만들어야하는데 위치를 어디에다 할
 */
