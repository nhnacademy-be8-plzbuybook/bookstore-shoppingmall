package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import org.springframework.data.domain.Page;


public interface MemberService {
    MemberCreateResponseDto createMember(MemberCreateRequestDto memberCreateRequestDto);
    MemberModifyResponseDto modify(Long memberId, MemberModifyRequestDto memberModifyRequestDto);
    MemberEmailResponseDto getMemberByEmail(String email);
    MemberIdResponseDto getMemberById(Long id);
    void withdrawMember(Long memberId);
    Page<MemberSearchResponseDto> getMembers(MemberSearchRequestDto memberSearchRequestDto);
}
