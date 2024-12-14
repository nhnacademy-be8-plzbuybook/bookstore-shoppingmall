package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberStateException;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import com.nhnacademy.book.member.domain.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberStatusServiceImpl implements MemberStatusService {

    private final MemberStatusRepository memberStatusRepository;

    @Override
    public MemberStatus createMemberStatus(MemberStatusCreateRequestDto memberStatusCreateRequestDto) {
        memberStatusRepository.findByMemberStateName(memberStatusCreateRequestDto.getMemberStateName())
                .ifPresent(existing -> {
                    throw new DuplicateMemberStateException("이미 존재하는 상태!");
                });

        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStateName(memberStatusCreateRequestDto.getMemberStateName());

        return memberStatusRepository.save(memberStatus);
    }
}
