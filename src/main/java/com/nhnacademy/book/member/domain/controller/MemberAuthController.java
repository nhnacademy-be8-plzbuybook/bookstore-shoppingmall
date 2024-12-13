package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.MemberAuth;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberAuthController {
    private MemberAuthService memberAuthService;

        // 회원 권한 할당
        @PostMapping("/members/auths")
        public ResponseEntity<String> assignAuthToMember(@RequestBody MemberAuthAssignRequestDto requestDto) {
            memberAuthService.assignAuthToMember(requestDto);
            return ResponseEntity.ok("권한이 할당되었습니다.");
        }

        // 회원 권한 조화
        @GetMapping("/members/{memberId}/auths")
        public List<MemberAuth> getMemberAuths(@PathVariable Long memberId) {
            return memberAuthService.getMemberAuthsByMemberId(memberId);
        }

        // 회원 권한 삭제
        @DeleteMapping("/members/{memberId}/auths")
        public void deleteMemberAuths(@PathVariable Long memberId) {
        memberAuthService.deleteMemberAuthsByMemberId(memberId);
        }
}

