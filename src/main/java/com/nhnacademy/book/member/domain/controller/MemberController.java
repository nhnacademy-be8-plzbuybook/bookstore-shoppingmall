package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
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





    //회원 등급 추가(값 추가를 위함)
    @PostMapping("/members/grade")
    public MemberGrade createMemberGrade (@RequestBody MemberGradeCreateRequestDto memberGradeCreateRequestDto) {
        MemberGrade memberGrade = memberService.save(memberGradeCreateRequestDto);

        return memberGrade;
    }

    //회원 상태 추가(값 추가를 위함)
    @PostMapping("/members/status")
    public MemberStatus createMemberStatus (@RequestBody MemberStatusCreateRequestDto memberStatusCreateRequestDto){
        MemberStatus memberStatus = memberService.save(memberStatusCreateRequestDto);

        return memberStatus;
    }
}
