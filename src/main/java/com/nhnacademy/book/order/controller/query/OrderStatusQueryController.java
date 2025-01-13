package com.nhnacademy.book.order.controller.query;

import com.nhnacademy.book.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderStatusQueryController {

    /**
     * 주문상태 목록조회
     *
     * @return
     */
    @GetMapping("/api/orders/order-status")
    public ResponseEntity<List<OrderStatus>> getOrderStatuses() {
        List<OrderStatus> orderStatuses = List.of(OrderStatus.values());
        return ResponseEntity.ok(orderStatuses);
    }
}
