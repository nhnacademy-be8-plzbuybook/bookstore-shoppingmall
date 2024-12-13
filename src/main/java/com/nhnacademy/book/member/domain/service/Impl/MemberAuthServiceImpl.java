package com.nhnacademy.book.member.domain.service.Impl;


import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAuth;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignResponseDto;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {
    private final MemberAuthRepository memberAuthRepository;
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    @Transactional
    public String assignAuthToMember(MemberAuthAssignRequestDto requestDto) {
        Long memberId = requestDto.getMemberId();
        Long authId = requestDto.getAuthId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new IllegalArgumentException("권한이 존재하지 않습니다."));

        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(member);
        memberAuth.setAuth(auth);

        memberAuthRepository.save(memberAuth);
        return "회원 권한이 성공적으로 설정되었습니다.";
    }


    public List<MemberAuthAssignResponseDto> getMemberAuthsByMemberId(Long memberId) {
        return memberAuthRepository.findByMemberMemberId(memberId).stream()
                .map(auth -> new MemberAuthAssignResponseDto(
                        auth.getMemberAuthId(),
                        auth.getMember().getMemberId(),
                        auth.getAuth().getAuthId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMemberAuthsByMemberId(Long memberId) {
        memberAuthRepository.deleteByMemberMemberId(memberId);
    }
}
