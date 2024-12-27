package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.feign.CouponClient;
import com.nhnacademy.book.feign.dto.BirthdayCouponRequestDto;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.BirthdayCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class BirthdayCouponServiceImpl implements BirthdayCouponService {
    private final MemberRepository memberRepository;
    private final CouponClient couponClient;

    @Transactional(readOnly = true)
    @Override
    public void issueBirthdayCoupons(int month, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Page<Member> members;

        do {
            members = memberRepository.findByBirthMonth(month, pageable);

            members.stream()
                    .map(m -> new BirthdayCouponRequestDto(m.getMemberId(), now))
                    .forEach(couponClient::issueBirthdayCoupon);

            pageable = pageable.next();
        } while (!members.isLast());
    }
}
