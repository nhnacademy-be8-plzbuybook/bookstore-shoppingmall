package com.nhnacademy.book.order.controller.query;

import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.service.command.OrderReturningService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderReturningQueryController {
    private final OrderReturningService orderReturningService;

    @GetMapping("/api/orders/order-returns")
    public ResponseEntity<Page<OrderReturnDto>> getOrderReturns(Pageable pageable) {
        Page<OrderReturnDto> orderReturnDtoPage = orderReturningService.getAllOrderReturns(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(orderReturnDtoPage);
    }

    @GetMapping("/api/orders/order-returns/{tracking-number}")
    public ResponseEntity<OrderReturnDto> getOrderReturn(@PathVariable("tracking-number") String trackingNumber) {
        OrderReturnDto orderReturnDto = orderReturningService.getOrderReturnByTrackingNumber(trackingNumber);
        return ResponseEntity.status(HttpStatus.OK).body(orderReturnDto);
    }
}
