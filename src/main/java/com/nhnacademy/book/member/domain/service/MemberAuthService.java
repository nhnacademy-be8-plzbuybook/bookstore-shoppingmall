package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAuth;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private MemberAuthRepository memberAuthRepository;
    private MemberRepository memberRepository;
    private AuthRepository authRepository;

    public MemberAuth assignMemberAuth(Long memberId, Long authId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("권한이 존재하지 않습니다."));

        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(member);
        memberAuth.setAuth(auth);

        return memberAuthRepository.save(memberAuth);
    }

    public List<Auth> getMemberAuths(Long memberId) {
        List<MemberAuth> memberAuths = memberAuthRepository.findByMemberId(memberId);
        return memberAuths.stream()
                .map(MemberAuth::getAuth)
                .collect(Collectors.toList());
    }

    public void deleteMemberAuth(Long memberAuthId) {
        memberAuthRepository.deleteById(memberAuthId);
    }

}
