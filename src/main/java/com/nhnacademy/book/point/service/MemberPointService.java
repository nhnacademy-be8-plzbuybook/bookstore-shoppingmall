package com.nhnacademy.book.point.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.point.dto.MemberMyPointRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.review.domain.Review;

public interface MemberPointService {
    void addSignUpPoint(Member member);
//    void addBookPurchasePoint(Member member, Long orderId, Integer orderStatus);
    MemberPointAddResponseDto addReviewPoint(Review review);
    MemberPointAddResponseDto addMemberPoint(MemberPointAddRequestDto requestDto);
}
