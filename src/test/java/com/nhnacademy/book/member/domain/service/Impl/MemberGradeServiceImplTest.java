package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberGradeException;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MemberGradeServiceImplTest {

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private MemberGradeServiceImpl memberGradeService;

    //등급을 정상적으로 추가 할 경우
    @Test
    void createGrade_Success() {
        String gradeName = "NORMAL";
        BigDecimal conditionPrice = BigDecimal.valueOf(0.5);
        LocalDateTime gradeChange = LocalDateTime.of(2000, 3, 9, 1, 1);
        MemberGradeCreateRequestDto memberGradeCreateRequestDto = new MemberGradeCreateRequestDto(gradeName, conditionPrice, gradeChange);

        //처음에 등록할 떄는 비어 있는게 맞다
        when(memberGradeRepository.findByMemberGradeName(gradeName)).thenReturn(Optional.empty());

        when(memberGradeRepository.save(any(MemberGrade.class))).thenAnswer(invocation -> {
            MemberGrade memberGrade = invocation.getArgument(0);
            memberGrade.setMemberGradeId(1L);
            return memberGrade;
        });

        MemberGrade savedGrade = memberGradeService.createMemberGrade(memberGradeCreateRequestDto);

        assertEquals(1L, savedGrade.getMemberGradeId());
        assertEquals(gradeName, savedGrade.getMemberGradeName());
        assertEquals(conditionPrice, savedGrade.getConditionPrice());
        assertEquals(gradeChange, savedGrade.getGradeChange());

        verify(memberGradeRepository, times(1)).save(any(MemberGrade.class));
        verify(memberGradeRepository, times(1)).findByMemberGradeName(gradeName);


    }

    //중복 등급을 추가하려 할 때 예외 처리
    @Test
    void createGrade_DuplicateMemberGradeException() {
        String gradeName = "NORMAL";
        BigDecimal conditionPrice = BigDecimal.valueOf(0.5);
        LocalDateTime gradeChange = LocalDateTime.of(2000, 3, 9, 1, 1);
        MemberGrade existingMemberGrade = new MemberGrade();

        MemberGradeCreateRequestDto memberGradeCreateRequestDto = new MemberGradeCreateRequestDto(gradeName, conditionPrice, gradeChange);

        existingMemberGrade.setMemberGradeId(1L);
        existingMemberGrade.setMemberGradeName(gradeName);
        existingMemberGrade.setConditionPrice(conditionPrice);
        existingMemberGrade.setGradeChange(gradeChange);


        when (memberGradeRepository.findByMemberGradeName(gradeName)).thenReturn(Optional.of(existingMemberGrade));

        assertThrows(DuplicateMemberGradeException.class,
                () -> memberGradeService.createMemberGrade(memberGradeCreateRequestDto));

        verify(memberGradeRepository, never()).save(any(MemberGrade.class));
        verify(memberGradeRepository, times(1)).findByMemberGradeName(gradeName);

    }
}