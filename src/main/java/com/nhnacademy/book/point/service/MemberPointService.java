package com.nhnacademy.book.point.service;

import com.nhnacademy.book.member.domain.Review;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;

public interface MemberPointService {
    MemberPointAddResponseDto addMemberPoint(MemberPointAddRequestDto memberPointAddRequestDto);
    void addReviewPoint(Review review);
}
