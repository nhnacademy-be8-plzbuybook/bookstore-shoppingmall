package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.coupon.dto.BirthdayCouponRequestDto;
import com.nhnacademy.book.coupon.service.CouponService;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirthdayCouponServiceImplTest {

    @InjectMocks
    private BirthdayCouponServiceImpl birthdayCouponService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CouponService couponService;

    private Member mockMember;
    private MemberGrade mockMemberGrade;
    private MemberStatus mockMemberStatus;
    private Page<Member> mockPage;


    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        mockMemberGrade = new MemberGrade(1L, "NORMAL", BigDecimal.valueOf(10000), now);
        mockMemberStatus = new MemberStatus(1L, "ACTIVE");
        mockMember = new Member(
                1L, mockMemberGrade, mockMemberStatus, "test", "010-0000-0000", "test@test.com", LocalDate.of(1999, 4, 5), "password"
        );
        Pageable pageable = PageRequest.of(0, 100);
        mockPage = new PageImpl<>(List.of(mockMember), pageable, 1);
    }

    @DisplayName("생일쿠폰 발급")
    @Test
    void issueBirthdayCoupons() {
        int month = 12;
        Pageable pageable = PageRequest.of(0, 100);

        when(memberRepository.findByBirthMonth(month, pageable)).thenReturn(mockPage);

        birthdayCouponService.issueBirthdayCoupons(month, pageable);

        verify(memberRepository, times(1)).findByBirthMonth(eq(month), any(Pageable.class));
        verify(couponService, times(1)).issueBirthdayCoupon(any(BirthdayCouponRequestDto.class));
    }

}