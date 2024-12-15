package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.service.MemberGradeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberGradeControllerTest {

    @Mock
    private MemberGradeService memberGradeService;

    @InjectMocks
    private MemberGradeController memberGradeController;

    @Test
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
}