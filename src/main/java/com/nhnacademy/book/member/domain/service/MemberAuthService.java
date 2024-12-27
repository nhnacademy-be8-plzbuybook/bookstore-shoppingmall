package com.nhnacademy.book.member.domain.service;


import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;

import java.util.List;


public interface MemberAuthService {
    MemberAuthResponseDto assignAuthToMember(MemberAuthRequestDto requestDto);
//    List<Long> getAuthsByMember(Long memberId);
    List<String> getAuthNameByMember(Long memberId);
    MemberAuthResponseDto updateMemberAuth(MemberAuthRequestDto requestDto);
    void deleteAuthFromMember(Long memberId, Long authId);
}