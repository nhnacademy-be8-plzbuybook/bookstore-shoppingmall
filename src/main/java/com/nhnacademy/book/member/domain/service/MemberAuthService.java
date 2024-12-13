package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAuth;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private MemberAuthRepository memberAuthRepository;
    private MemberRepository memberRepository;
    private AuthRepository authRepository;

    @Transactional
    public String assignAuthToMember(MemberAuthAssignRequestDto requestDto) {
        Long memberId = requestDto.getMemberId();
        Long authId = requestDto.getAuthId();

        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Auth> auth = authRepository.findById(authId);

        if (member.isEmpty() || auth.isEmpty()) {
            throw new IllegalArgumentException("회원 또는 권한이 존재하지 않습니다.");
        }

        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(member.get());
        memberAuth.setAuth(auth.get());

        memberAuthRepository.save(memberAuth);
        return "회원 권한이 성공적으로 할당되었습니다.";
    }

    public List<MemberAuth> getMemberAuthsByMemberId(Long memberId) {
        return memberAuthRepository.findByMemberMemberId(memberId);
    }

    public void deleteMemberAuthsByMemberId(Long memberId) {
        memberAuthRepository.deleteByMemberMemberId(memberId);
    }
}

