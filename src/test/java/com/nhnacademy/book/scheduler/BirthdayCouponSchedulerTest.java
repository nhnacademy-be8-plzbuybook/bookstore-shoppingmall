package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.BirthdayCouponService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BirthdayCouponSchedulerTest {

    @InjectMocks
    private BirthdayCouponScheduler birthdayCouponScheduler;

    @Mock
    private BirthdayCouponService birthdayCouponService;

    @DisplayName("스케줄러로 생일쿠폰 발급")
    @Test
    void birthdayCoupon() {
        LocalDate mockDate = LocalDate.of(2024, 12, 1);
        int month = mockDate.getMonthValue();
        Pageable pageable = PageRequest.of(0, 100, Sort.by("memberId"));

        try (var mockedStatic = Mockito.mockStatic(LocalDate.class)) {
            mockedStatic.when(LocalDate::now).thenReturn(mockDate);

            birthdayCouponScheduler.birthdayCoupon();

            verify(birthdayCouponService, times(1)).issueBirthdayCoupons(month, pageable);
        }
    }


}