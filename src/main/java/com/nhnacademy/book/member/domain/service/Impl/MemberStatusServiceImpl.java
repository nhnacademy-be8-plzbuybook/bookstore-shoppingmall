package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberStateException;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberStatusNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import com.nhnacademy.book.member.domain.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberStatusServiceImpl implements MemberStatusService {

    private final MemberRepository memberRepository;
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

    //휴면 계정 잠금해제 DORMANT => ACTIVE
    @Override
    public void updateMemberStatusActiveByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("이메일에 해당하는 회원을 찾을 수 없다!"));

        MemberStatus activeStatus = memberStatusRepository.findByMemberStateName("ACTIVE")
                .orElseThrow(() -> new MemberStatusNotFoundException("존재하지 않는 상태!"));

        member.setMemberStatus(activeStatus);

        memberRepository.save(member);
    }
}
