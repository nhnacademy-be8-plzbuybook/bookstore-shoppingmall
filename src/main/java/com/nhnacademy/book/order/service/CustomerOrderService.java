package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.CustomerOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.MemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.NonMemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;

public interface CustomerOrderService {
    Long placeCustomerOrder(String orderId, OrderRequestDto orderRequest);
    Long placeMemberOrder(MemberOrderSaveRequestDto saveRequest);
    Long placeNonMemberOrder(NonMemberOrderSaveRequestDto saveRequest);
}
