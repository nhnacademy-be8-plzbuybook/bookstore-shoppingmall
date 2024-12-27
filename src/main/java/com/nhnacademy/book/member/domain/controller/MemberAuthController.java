package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberAuthController {
    private final MemberAuthService memberAuthService;

    // 회원 권한 설정
    @PostMapping("/members/auths")
    public ResponseEntity<MemberAuthResponseDto> assignAuthToMember(@RequestBody MemberAuthRequestDto requestDto) {
        MemberAuthResponseDto responseDto = memberAuthService.assignAuthToMember(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 회원 권한 조회
    @GetMapping("/members/{member_id}/auths")
    public ResponseEntity<List<String>> getAuthNameByMember(@PathVariable Long member_id) {
        List<String> authNames = memberAuthService.getAuthNameByMember(member_id);
        return new ResponseEntity<>(authNames, HttpStatus.OK);
    }

    // 회원 권한 수정
    @PutMapping("/members/{member_id}/auths")
    public ResponseEntity<MemberAuthResponseDto> updateMemberAuth(@PathVariable Long member_id, @RequestBody MemberAuthRequestDto requestDto) {
        requestDto.setMemberId(member_id);
        MemberAuthResponseDto responseDto = memberAuthService.updateMemberAuth(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 회원 권한 삭제
    @DeleteMapping("/members/{member_id}/auths/{auth_id}")
    public ResponseEntity<Void> deleteMemberAuth(@PathVariable Long member_id, @PathVariable Long auth_id) {
        memberAuthService.deleteAuthFromMember(member_id, auth_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

