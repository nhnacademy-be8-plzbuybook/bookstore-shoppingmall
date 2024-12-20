package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.service.MemberGradeService;
import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.member.domain.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원 생성
    @PostMapping("/members")
    public ResponseEntity<MemberCreateResponseDto> createMember(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        MemberCreateResponseDto responseDto = memberService.createMember(memberCreateRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    //회원 수정
    @PutMapping("/members/{member_id}")
    public ResponseEntity<MemberModifyResponseDto> modifyMember(
            @PathVariable Long member_id,
            @RequestBody MemberModifyRequestDto memberModifyRequestDto) {

        MemberModifyResponseDto responseDto = memberService.modify(member_id, memberModifyRequestDto);
        return ResponseEntity.ok(responseDto);
    }


    //특정 회원 조회(이메일)
    @GetMapping("/members/email")
    public ResponseEntity<MemberEmailResponseDto> getMemberByEmail(@RequestParam String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }


    //특정 회원 조회(id)
    @GetMapping("/members/{member_id}")
    public ResponseEntity<MemberIdResponseDto> getMemberById(@PathVariable Long member_id) {
        return ResponseEntity.ok(memberService.getMemberById(member_id));
    }

    //회원 삭제( => withdrawal로 상태 변경)
    @PutMapping("/members/{member_id}/withdrawal")
    public ResponseEntity<String> withdrawMember(@PathVariable Long member_id) {
        memberService.withdrawMember(member_id);
        return ResponseEntity.ok("탈퇴 처리 됐습니다.");
    }

    //전체 회원 조회
    @GetMapping("/members")
    public ResponseEntity<Page<MemberSearchResponseDto>> getMembers(@ModelAttribute MemberSearchRequestDto memberSearchRequestDto) {
        return ResponseEntity.ok(memberService.getMembers(memberSearchRequestDto));
    }
}
