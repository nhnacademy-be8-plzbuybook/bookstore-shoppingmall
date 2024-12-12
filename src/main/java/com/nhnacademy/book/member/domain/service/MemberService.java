package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;



public interface MemberService {
    Member save(MemberCreateRequestDto memberCreateRequestDto);
    MemberGrade save(MemberGradeCreateRequestDto memberGradeCreateRequestDto);
    MemberStatus save(MemberStatusCreateRequestDto memberStatusCreateRequestDto);
    MemberGrade findByMemberGradeId(Long id);
    MemberStatus findByMemberStatusId(Long id);
}
