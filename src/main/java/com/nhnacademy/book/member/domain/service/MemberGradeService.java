package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;

public interface MemberGradeService {
    MemberGrade createMemberGrade(MemberGradeCreateRequestDto memberGradeCreateRequestDto);
}
