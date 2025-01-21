package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.dto.MemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.NonMemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.entity.MemberOrder;
import com.nhnacademy.book.order.entity.NonMemberOrder;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.repository.MemberOrderRepository;
import com.nhnacademy.book.order.repository.NonMemberOrderRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
    private final NonMemberOrderRepository nonMemberOrderRepository;
    private final MemberOrderRepository memberOrderRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Long placeCustomerOrder(String orderId, OrderRequestDto orderRequest) {
        if (orderRequest.getOrderType() == OrderType.MEMBER_ORDER) {
            return placeMemberOrder(new MemberOrderSaveRequestDto(orderRequest.getMemberEmail(), orderId));
        } else if (orderRequest.getOrderType() == OrderType.NON_MEMBER_ORDER) {
            return placeNonMemberOrder(new NonMemberOrderSaveRequestDto(orderId, orderRequest.getNonMemberPassword()));
        }
        throw new RuntimeException("잘못된 요청파라미터입니다.");
    }


    @Override
    public Long placeMemberOrder(MemberOrderSaveRequestDto saveRequest) {
        Orders order = orderRepository.findById(saveRequest.getOrderId()).orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다."));
        Member member = memberRepository.findByEmail(saveRequest.getMemberEmail()).orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));
        MemberOrder savedMemberOrder = memberOrderRepository.save(new MemberOrder(member, order));
        return savedMemberOrder.getId();
    }

    @Override
    public Long placeNonMemberOrder(NonMemberOrderSaveRequestDto saveRequest) {
        Orders order = orderRepository.findById(saveRequest.getOrderId()).orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다."));
        NonMemberOrder savedNonMemberOrder = nonMemberOrderRepository.save(new NonMemberOrder(order, saveRequest.getNonMemberOrderPassword()));
        return savedNonMemberOrder.getId();
    }
}
