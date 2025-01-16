package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderReturningService {
    //    String requestOrderReturn(String orderId, OrderReturnRequestDto refundRequest);

    String completeOrderReturn(String orderId);

//    OrderReturnDto getOrderReturnByTrackingNumber(String trackingNumber);

//    Page<OrderReturnDto> getAllOrderReturns(OrderReturnSearchRequestDto searchRequest, Pageable pageable);
    Page<OrderProductReturnDto> getAllOrderProductReturns(OrderReturnSearchRequestDto searchRequest, Pageable pageable);

    void requestOrderProductReturn(String orderId, Long orderProductId, OrderProductReturnRequestDto orderProductReturnRequest);

    Long completeOrderProductReturn(Long orderProductId);

}
