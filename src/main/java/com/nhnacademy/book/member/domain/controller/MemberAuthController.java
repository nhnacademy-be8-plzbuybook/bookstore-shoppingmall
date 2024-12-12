package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.MemberAuth;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberAuthController {
    private MemberAuthService memberAuthService;

    @PostMapping("members/auths")
    public ResponseEntity<MemberAuth> assignMemberAuth(@RequestBody MemberAuthRequestDto request) {
        MemberAuth memberAuth = memberAuthService.assignMemberAuth(request.getMemberId(), request.getAuthId());
        return ResponseEntity.status(HttpStatus.CREATED).body(memberAuth);
    }

    @GetMapping("/members/{memberId}/auths")
    public ResponseEntity<List<Auth>> getMemberAuth(@PathVariable Long memberId) {
        List<Auth> auths = memberAuthService.getMemberAuths(memberId);
        return ResponseEntity.ok(auths);
    }

    @DeleteMapping("/members/{memberId}/auths")
    public ResponseEntity<Void> deleteMemberAuth(@PathVariable Long memberAuthId) {
        memberAuthService.deleteMemberAuth(memberAuthId);
        return ResponseEntity.noContent().build();
    }

}