package com.nhnacademy.book.order.controller.command;

import com.nhnacademy.book.order.service.command.OrderProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderProcessController {
    private final OrderProcessService orderProcessService;


    /**
     * 주문 완료
     *
     * @param orderId 주문 ID
     * @return 주문 ID
     */
    @PostMapping("/{order-id}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable("order-id") String orderId) {
        orderProcessService.completeOrder(orderId);

        return ResponseEntity.ok(orderId);
    }
}
