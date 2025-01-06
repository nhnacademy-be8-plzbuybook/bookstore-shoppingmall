package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/members/{email}/active")
    public ResponseEntity<String> activateMemberStatus(@PathVariable String email){
        try{
            memberStatusService.updateMemberStatusActiveByEmail(email);
            return ResponseEntity.ok().body("회원 상태가 active로 변경!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원 상태 변경 실패 " + e.getMessage());
        }
    }

    @GetMapping("/members/status/all")
    public List<MemberStatus> getAllMemberStatus(){
        return memberStatusService.getAllMemberStatuses();
    }

}
