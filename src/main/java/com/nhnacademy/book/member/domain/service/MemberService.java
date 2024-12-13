package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;


public interface MemberService {
    Member save(MemberCreateRequestDto memberCreateRequestDto);
    Member modify(Long memberId, MemberModifyRequestDto memberModifyRequestDto);
    MemberGrade save(MemberGradeCreateRequestDto memberGradeCreateRequestDto);
    MemberStatus save(MemberStatusCreateRequestDto memberStatusCreateRequestDto);
    MemberGrade findByMemberGradeId(Long id);
    MemberStatus findByMemberStatusId(Long id);
    MemberEmailResponseDto getMemberByEmail(String email);
}
