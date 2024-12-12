package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.dto.MemberCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberCreateResponseDto;
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

    @PostMapping("/members")
    public MemberCreateResponseDto createMember(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        Member member = memberService.save(memberCreateRequestDto);

        MemberCreateResponseDto memberCreateResponseDto = new MemberCreateResponseDto(
                member.getName(),
                member.getPhone(),
                member.getEmail(),
                member.getBirth()
        );

        return memberCreateResponseDto;
    }

}
