package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.BirthdayCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@RequiredArgsConstructor
@Component
public class BirthdayCouponScheduler {

    private final BirthdayCouponService birthdayCouponService;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void birthdayCoupon() {
        int month = LocalDate.now().getMonthValue();
        Pageable pageable = PageRequest.of(0, 100, Sort.by("memberId"));
        birthdayCouponService.issueBirthdayCoupons(month, pageable);
    }
}
