package com.nhnacademy.book.member.domain.service;


import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;

import java.util.List;


public interface MemberAuthService {
    // 회원에게 권한 부여
    MemberAuthResponseDto assignAuthToMember(MemberAuthRequestDto requestDto);

    // 회원의 권한 조회
    List<Long> getAuthsByMember(Long memberId);

    // 회원 권한 수정
    MemberAuthResponseDto updateMemberAuth(MemberAuthRequestDto requestDto);

    // 회원 권한 제거
    void deleteAuthFromMember(Long memberId, Long authId);
}