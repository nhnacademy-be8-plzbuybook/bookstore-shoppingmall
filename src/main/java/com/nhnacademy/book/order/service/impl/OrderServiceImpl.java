package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.order.dto.OrderDto;
import com.nhnacademy.book.order.dto.OrderSearchRequestDto;
import com.nhnacademy.book.order.repository.OrderQueryRepository;
import com.nhnacademy.book.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderQueryRepository orderQueryRepository;

    // 전체 주문 조회
    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> getOrders(OrderSearchRequestDto searchRequest, Pageable pageable) {
        return orderQueryRepository.findAllOrders(searchRequest.getMemberId(), searchRequest.getProductName(),
                searchRequest.getOrderDate(), searchRequest.getOrderStatus(), pageable);
    }

}
