package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;


public interface MemberService {
    MemberCreateResponseDto createMember(MemberCreateRequestDto memberCreateRequestDto);
    MemberModifyResponseDto modify(Long memberId, MemberModifyRequestDto memberModifyRequestDto);
    MemberGrade save(MemberGradeCreateRequestDto memberGradeCreateRequestDto);
    MemberStatus save(MemberStatusCreateRequestDto memberStatusCreateRequestDto);
    MemberEmailResponseDto getMemberByEmail(String email);
    MemberIdResponseDto getMemberById(Long id);
    void withdrawMember(Long memberId);
}
