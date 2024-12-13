package com.nhnacademy.book.member.domain.service;


import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignResponseDto;

import java.util.List;


public interface MemberAuthService {
    String assignAuthToMember(MemberAuthAssignRequestDto requestDto);
    List<MemberAuthAssignResponseDto> getMemberAuthsByMemberId(Long memberId);
    void deleteMemberAuthsByMemberId(Long memberId);
}
