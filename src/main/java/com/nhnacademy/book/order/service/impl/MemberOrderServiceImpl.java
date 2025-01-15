package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.dto.MemberOrderSaveRequestDto;
import com.nhnacademy.book.order.entity.MemberOrder;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.MemberOrderRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberOrderServiceImpl {
    private final MemberOrderRepository memberOrderRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long placeOrder(MemberOrderSaveRequestDto saveRequest) {
        Orders order = orderRepository.findById(saveRequest.getOrderId()).orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다."));
        Member member = memberRepository.findByEmail(saveRequest.getMemberEmail()).orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));
        MemberOrder saved = memberOrderRepository.save(new MemberOrder(member, order));
        return saved.getId();
    }
}
