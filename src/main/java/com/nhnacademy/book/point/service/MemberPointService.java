package com.nhnacademy.book.point.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;

public interface MemberPointService {
    void addSignUpPoint(Member member);
//    void addBookPurchasePoint(Member member, Long orderId, Integer orderStatus);
//    void addReviewPoint(Review review);
    MemberPointAddResponseDto addMemberPoint(MemberPointAddRequestDto requestDto);



}
