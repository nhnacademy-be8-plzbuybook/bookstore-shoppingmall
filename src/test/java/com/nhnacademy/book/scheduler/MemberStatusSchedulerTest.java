package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberStatusSchedulerTest {

    @Mock
    private MemberService memberService; // Mock 객체

    @InjectMocks
    private MemberStatusScheduler memberStatusScheduler; // 테스트할 스케줄러 클래스

    @DisplayName("회원 상태 스케줄러가 updateDormantStatus를 호출하는지 확인")
    @Test
    void updateDormantStatusDaily_ExecutesMemberServiceUpdate() {
        // 스케줄러 메서드 직접 호출
        memberStatusScheduler.updateDormantStatusDaily();

        // MemberService의 updateDormantStatus()가 호출되었는지 검증
        verify(memberService, times(1)).updateDormantStatus();
    }

}