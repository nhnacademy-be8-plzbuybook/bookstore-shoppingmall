package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.NonMemberOrderSaveRequestDto;
import com.nhnacademy.book.order.entity.NonMemberOrder;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.NonMemberOrderRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NonMemberOrderServiceImpl{
    private final NonMemberOrderRepository nonMemberOrderRepository;
    private final OrderRepository orderRepository;

    /**
     * 비회원 주문 저장
     *
     * @param saveRequest 주문ID, 비회원주문 비밀번호가 포함된 DTo
     * @return 저장된 비회원주문 ID
     */
    public Long placeOrder(NonMemberOrderSaveRequestDto saveRequest) {
        if (saveRequest.getNonMemberOrderPassword().isBlank()) {
            throw new IllegalArgumentException("비회원 주문 비밀번호가 제대로 설정되지 않았습니다.");
        }
        Orders order = orderRepository.findById(saveRequest.getOrderId()).orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다."));
        NonMemberOrder saved = nonMemberOrderRepository.save(new NonMemberOrder(order, saveRequest.getNonMemberOrderPassword()));
        return saved.getId();
    }
}
