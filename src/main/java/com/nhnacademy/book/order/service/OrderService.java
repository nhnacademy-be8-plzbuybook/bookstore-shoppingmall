package com.nhnacademy.book.order.service;

import com.nhnacademy.book.order.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getOrders(OrderSearchRequestDto searchRequest, Pageable pageable);
    OrderDetail getOrderDetail(String orderId);
    void orderDelivered(String orderId);
    String getNonMemberOrderId(NonMemberOrderDetailAccessRequestDto accessRequest);
    void modifyStatus(String orderId, OrderStatusModifyRequestDto modifyRequest);

}
