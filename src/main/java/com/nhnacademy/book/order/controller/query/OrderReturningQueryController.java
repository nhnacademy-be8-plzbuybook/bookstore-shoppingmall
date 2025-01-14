package com.nhnacademy.book.order.controller.query;

import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.service.command.OrderReturningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderReturningQueryController {
    private final OrderReturningService orderReturningService;

    @GetMapping("/api/orders/order-returns")
    public ResponseEntity<OrderReturnDto> getOrderReturn(@RequestParam("tracking-number") String trackingNumber) {
        OrderReturnDto orderReturnDto = orderReturningService.getByTrackingNumber(trackingNumber);
        return ResponseEntity.status(HttpStatus.FOUND).body(orderReturnDto);
    }
}
