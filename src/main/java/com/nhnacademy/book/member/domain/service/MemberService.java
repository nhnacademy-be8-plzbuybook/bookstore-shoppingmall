package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import org.springframework.data.domain.Page;


public interface MemberService {
    MemberCreateResponseDto createMember(MemberCreateRequestDto memberCreateRequestDto);
    MemberModifyResponseDto modify(Long memberId, MemberModifyRequestDto memberModifyRequestDto);
    void updateMember(String email, MemberModifyRequestDto memberModifyRequestDto);
    MemberEmailResponseDto getMemberByEmail(String email);
    MemberDto getMemberMyByEmail(String email);
    MemberIdResponseDto getMemberById(Long id);
    void withdrawMember(Long memberId);
    void withdrawState(String email);
    Page<MemberSearchResponseDto> getMembers(MemberSearchRequestDto memberSearchRequestDto);
    void updateActiveStatus(String email);
    void updateDormantStatus();
}
