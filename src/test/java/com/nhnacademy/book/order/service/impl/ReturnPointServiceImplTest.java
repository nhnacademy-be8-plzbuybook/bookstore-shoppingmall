package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReturnPointServiceImplTest {

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private MemberPointService memberPointService;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReturnPointServiceImpl returnPointService;

    private OrderProduct orderProduct;
    private Orders order;
    private Member member;
    private MemberGrade memberGrade;
    private MemberStatus memberStatus;
    private OrderProductReturn orderProductReturn;

    @BeforeEach
    void setUp() {
        // Mock 데이터 준비
        orderProduct = OrderProduct.builder()
                .id(1L)
                .price(new BigDecimal("10000"))
                .quantity(2)
                .couponDiscount(new BigDecimal("500"))
                .build();

        order = new Orders();
        order.setUsedPoint(1000);

        memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        memberStatus = new MemberStatus(1L, "ACTIVE");
        member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");

        orderProductReturn = OrderProductReturn.builder()
                .id(1L)
                .reason("Defective item")
                .trackingNumber("TRACK123")
                .quantity(1)
                .requestedAt(LocalDateTime.now())
                .completedAt(null)
                .orderProduct(orderProduct)
                .build();
    }

    @DisplayName("포인트 환불 로직 테스트")
    @Test
    void testReturnPoint() {
        Long orderProductId = 1L;
        Long memberId = 1L;
        Long memberOrderId = 1L;

        when(orderProductRepository.findOrderByOrderProductId(orderProductId)).thenReturn(order);
        when(orderProductRepository.findMemberIdByOrderProductId(orderProductId)).thenReturn(memberId);
        when(orderProductRepository.findMemberOrderIdByOrderProductId(orderProductId)).thenReturn(memberOrderId);
        when(orderProductRepository.findByMemberOrderId(memberId, memberOrderId))
                .thenReturn(List.of(orderProduct));
        when(orderProductRepository.findByOrderProductOrderProductId(orderProductId))
                .thenReturn(orderProductReturn);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(orderProduct));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        returnPointService.returnPoint(orderProductId);

        verify(orderProductRepository, times(1)).findOrderByOrderProductId(orderProductId);
        verify(orderProductRepository, times(1)).findMemberIdByOrderProductId(orderProductId);
        verify(orderProductRepository, times(1)).findMemberOrderIdByOrderProductId(orderProductId);
        verify(orderProductRepository, times(1)).findByMemberOrderId(memberId, memberOrderId);
        verify(orderProductRepository, times(1)).findByOrderProductOrderProductId(orderProductId);

        verify(memberPointService, times(1)).restorePoint(eq(member), anyInt());
    }

}