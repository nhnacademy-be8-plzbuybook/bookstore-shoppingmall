package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthAssignResponseDto;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberAuthController {
    private MemberAuthService memberAuthService;

    // 회원 권한 설정
    @PostMapping("/members/auths")
    public ResponseEntity<String> assignAuthToMember(@RequestBody MemberAuthAssignRequestDto requestDto) {
        memberAuthService.assignAuthToMember(requestDto);
        return ResponseEntity.ok("권한이 설정되었습니다.");
    }

    // 회원 권한 조회
    @GetMapping("/members/{memberId}/auths")
    public ResponseEntity<List<MemberAuthAssignResponseDto>> getMemberAuths(@PathVariable Long memberId) {
        List<MemberAuthAssignResponseDto> memberAuths = memberAuthService.getMemberAuthsByMemberId(memberId);
        return ResponseEntity.ok(memberAuths);
    }

    // 회원 권한 삭제
    @DeleteMapping("/members/{memberId}/auths")
    public ResponseEntity<Void> deleteMemberAuths(@PathVariable Long memberId) {
        memberAuthService.deleteMemberAuthsByMemberId(memberId);
        return ResponseEntity.noContent().build();
    }

}

