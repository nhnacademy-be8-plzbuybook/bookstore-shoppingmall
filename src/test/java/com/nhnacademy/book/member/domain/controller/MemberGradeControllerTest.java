package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.service.MemberGradeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberGradeControllerTest {

    @Mock
    private MemberGradeService memberGradeService;

    @InjectMocks
    private MemberGradeController memberGradeController;

    @Test
    @DisplayName("회원 등급 생성 controller test")
    void createMemberGrade() {
        MemberGradeCreateRequestDto memberGradeCreateRequestDto = new MemberGradeCreateRequestDto();
        memberGradeCreateRequestDto.setMemberGradeName("NORMAL");
        memberGradeCreateRequestDto.setConditionPrice(new BigDecimal("10.00"));
        memberGradeCreateRequestDto.setGradeChange(LocalDateTime.of(2000,3,9,11,11));

        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeId(1L);
        memberGrade.setMemberGradeName(memberGradeCreateRequestDto.getMemberGradeName());
        memberGrade.setConditionPrice(memberGradeCreateRequestDto.getConditionPrice());
        memberGrade.setGradeChange(memberGradeCreateRequestDto.getGradeChange());

        when(memberGradeService.createMemberGrade(memberGradeCreateRequestDto)).thenReturn(memberGrade);

        MemberGrade responseMemberGrade = memberGradeController.createMemberGrade(memberGradeCreateRequestDto);

        assertNotNull(responseMemberGrade);
        assertEquals(memberGrade.getMemberGradeId(), responseMemberGrade.getMemberGradeId());
        assertEquals(memberGrade.getMemberGradeName(), responseMemberGrade.getMemberGradeName());
        assertEquals(memberGrade.getConditionPrice(), responseMemberGrade.getConditionPrice());
        assertEquals(memberGrade.getGradeChange(), responseMemberGrade.getGradeChange());
    }

    @Test
    @DisplayName("회원 등급 전체 조회 controller test")
    void getAllMemberGrades() {
        MemberGrade memberGrade1 = new MemberGrade(1L, "test", new BigDecimal("1000.00"), LocalDateTime.now());
        MemberGrade memberGrade2 = new MemberGrade(2L, "test2", new BigDecimal("1000.00"), LocalDateTime.now());

        List<MemberGrade> memberGrades = Arrays.asList(memberGrade1, memberGrade2);

        when(memberGradeService.getAllMemberGrades()).thenReturn(memberGrades);
        List<MemberGrade> responseMemberGrades = memberGradeController.getAllMemberGrades();

        assertNotNull(responseMemberGrades);
        assertEquals(memberGrades.size(), responseMemberGrades.size());
        assertEquals(memberGrades.get(0).getMemberGradeId(), responseMemberGrades.get(0).getMemberGradeId());
        assertEquals(memberGrades.get(1).getMemberGradeName(), responseMemberGrades.get(1).getMemberGradeName());
    }
}