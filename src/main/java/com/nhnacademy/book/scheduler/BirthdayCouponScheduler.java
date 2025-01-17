package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.BirthdayCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class BirthdayCouponScheduler {

    private final BirthdayCouponService birthdayCouponService;

    // 매일 00:00에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void birthdayCoupon() {
        log.info("스케줄러 실행 시작");
        int day = LocalDate.now().getDayOfMonth();

        // 매달 1일인 경우 실행
        if (day == 1) {
            int month = LocalDate.now().getMonthValue();
            Pageable pageable = PageRequest.of(0, 100, Sort.by("memberId"));
            birthdayCouponService.issueBirthdayCoupons(month, pageable);
        }
        log.info("스케줄러 실행 종료");
    }
}
