package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberCreateResponseDto;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    //회원 추가
    @PostMapping("/members")
    public MemberCreateResponseDto createMember(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        Member member = memberService.save(memberCreateRequestDto);

        MemberGrade memberGrade = memberService.findByMemberGradeId(memberCreateRequestDto.getMemberGradeId());
        MemberStatus memberStatus = memberService.findByMemberStatusId(memberCreateRequestDto.getMemberStateId());

        MemberCreateResponseDto memberCreateResponseDto = new MemberCreateResponseDto(
                member.getName(),
                member.getPhone(),
                member.getEmail(),
                member.getBirth(),
                memberGrade.getMemberGradeName(),
                memberStatus.getMemberStateName()
        );

        return memberCreateResponseDto;
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
