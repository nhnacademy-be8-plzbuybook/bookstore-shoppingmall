package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MemberStatusScheduler {

    private final MemberService memberService;

    @Scheduled(cron = "0/30 02 16 * * ?")
    public void updateDormantStatusDaily() {
        log.info("스케쥴러 실행 시작");
        memberService.updateDormantStatus();
        log.info("스케줄러 실행 완료");
}

}
