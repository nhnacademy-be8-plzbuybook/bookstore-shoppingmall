package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.MemberGradeService;
import com.nhnacademy.book.member.domain.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberGradeSchedulerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberGradeScheduler memberGradeScheduler;

    @DisplayName("회원 등급 스케줄러")
    @Test
    void updateDormantStatusDaily_ExecutesMemberGradeServiceUpdate() {
        // 스케줄러 메서드 직접 호출
        memberGradeScheduler.updateDormantStatusDaily();

        // MemberService의 updateMemberGrades()가 호출되었는지 검증
        verify(memberService, times(1)).updateMemberGrades();
    }

}