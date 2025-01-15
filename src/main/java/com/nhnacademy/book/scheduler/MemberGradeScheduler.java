package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberGradeScheduler {

    private final MemberService memberService;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    public void updateDormantStatusDaily() {
        memberService.updateMemberGrades();
    }
}
