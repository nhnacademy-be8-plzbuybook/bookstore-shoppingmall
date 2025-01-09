package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    Page<OrderDto> getOrders(OrderSearchRequestDto searchRequest, Pageable pageable);
    OrderDetail getOrderDetail(String orderId);

    @Transactional
    void orderDelivered(String orderId);

    NonMemberOrderDetail getNonMemberOrderDetail(NonMemberOrderDetailAccessRequestDto accessRequest);
    String getNonMemberOrder(NonMemberOrderDetailAccessRequestDto accessRequest);
    void patchStatus(String orderId, StatusDto patchRequest);

}
