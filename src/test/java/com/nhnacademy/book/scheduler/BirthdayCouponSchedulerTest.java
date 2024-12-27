package com.nhnacademy.book.scheduler;

import com.nhnacademy.book.member.domain.service.BirthdayCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        int month = LocalDate.now().getMonthValue();
        Pageable expectedPageable = PageRequest.of(0, 100, Sort.by("memberId"));
        birthdayCouponScheduler.birthdayCoupon();
        verify(birthdayCouponService, times(1)).issueBirthdayCoupons(eq(month), eq(expectedPageable));
    }
}