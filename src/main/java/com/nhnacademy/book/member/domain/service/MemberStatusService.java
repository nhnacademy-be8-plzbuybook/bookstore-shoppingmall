package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;

import java.util.List;

public interface MemberStatusService {
    MemberStatus createMemberStatus(MemberStatusCreateRequestDto memberStatusCreateRequestDto);
    void updateMemberStatusActiveByEmail(String email);
    List<MemberStatus> getAllMemberStatuses();
}
