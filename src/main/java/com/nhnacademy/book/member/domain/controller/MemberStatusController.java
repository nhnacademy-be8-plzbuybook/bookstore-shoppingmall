package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberStatusController {

    private final MemberStatusService memberStatusService;

    //회원 상태 추가(값 추가를 위함)
    @PostMapping("/members/status")
    public MemberStatus createMemberStatus (@RequestBody MemberStatusCreateRequestDto memberStatusCreateRequestDto){
        MemberStatus memberStatus = memberStatusService.createMemberStatus(memberStatusCreateRequestDto);

        return memberStatus;
    }
}
