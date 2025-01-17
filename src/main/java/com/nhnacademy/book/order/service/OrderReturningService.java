package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderReturningService {
    Page<OrderProductReturnDto> getAllOrderProductReturns(OrderReturnSearchRequestDto searchRequest, Pageable pageable);

    void requestOrderProductReturn(String orderId, Long orderProductId, OrderProductReturnRequestDto orderProductReturnRequest);

    Long completeOrderProductReturn(Long orderProductId);

}
