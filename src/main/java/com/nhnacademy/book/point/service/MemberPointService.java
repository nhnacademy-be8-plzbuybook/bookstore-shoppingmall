package com.nhnacademy.book.point.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;
import com.nhnacademy.book.review.domain.Review;

import java.math.BigDecimal;
import java.util.List;

public interface MemberPointService {
    void addSignUpPoint(Member member);
    void addPurchasePoint(Member member, OrderRequestDto orderRequest);
    void addReviewPoint(Review review);
    MemberPointAddResponseDto addMemberPoint(MemberPointAddRequestDto requestDto);
    List<MemberPointListResponseDto> getMemberPointsByMemberId(Long memberId);
    int getAvailablePoints(String email);
    void usedPoint(String email, Integer usedPoint);
    void updatePointForReview(Review review, boolean isPhotoAdded);




}