package com.nhnacademy.book.member.domain.service;


import org.springframework.data.domain.Pageable;

public interface BirthdayCouponService  {

    void issueBirthdayCoupons(int month, Pageable pageable); // 생일쿠폰 발급

}
