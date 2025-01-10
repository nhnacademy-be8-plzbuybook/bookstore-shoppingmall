package com.nhnacademy.book.point.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface MemberPointService {
    void addSignUpPoint(Member member);
//    void addBookPurchasePoint(Member member, Long orderId, Integer orderStatus);
//    void addReviewPoint(Review review);
    MemberPointAddResponseDto addMemberPoint(MemberPointAddRequestDto requestDto);
    List<MemberPointListResponseDto> getMemberPointsByMemberId(Long memberId);
    int getAvailablePoints(String email);
    void usedPoint(String email, Integer usedPoint);




}
