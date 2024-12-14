package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;

public interface MemberStatusService {
    MemberStatus createMemberStatus(MemberStatusCreateRequestDto memberStatusCreateRequestDto);
}
