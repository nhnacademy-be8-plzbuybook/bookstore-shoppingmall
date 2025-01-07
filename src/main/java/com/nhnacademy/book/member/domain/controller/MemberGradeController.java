package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.service.MemberGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberGradeController {

    private final MemberGradeService memberGradeService;

    //회원 등급 추가(값 추가를 위함)
    @PostMapping("/members/grade")
    public MemberGrade createMemberGrade (@RequestBody MemberGradeCreateRequestDto memberGradeCreateRequestDto) {
        MemberGrade memberGrade = memberGradeService.createMemberGrade(memberGradeCreateRequestDto);

        return memberGrade;
    }

    @GetMapping("/members/grade/all")
    public List<MemberGrade> getAllMemberGrades() {
        return memberGradeService.getAllMemberGrades();
    }
}
