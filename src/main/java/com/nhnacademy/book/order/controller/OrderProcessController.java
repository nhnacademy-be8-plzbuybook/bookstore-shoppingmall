package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.service.OrderProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderProcessController {
    private final OrderProcessService orderProcessService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> requestMemberOrder(@RequestHeader("X-USER-ID") String xUserId,
                                                               @Valid @RequestBody MemberOrderRequestDto memberOrderRequest) {
        memberOrderRequest.setMemberEmail(xUserId);
        OrderResponseDto orderResponse = orderProcessService.processRequestedOrder(memberOrderRequest);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @PostMapping("/non-member")
    public ResponseEntity<OrderResponseDto> requestNonMemberOrder(@Valid @RequestBody NonMemberOrderRequestDto nonMemberOrderRequest) {
        OrderResponseDto orderResponse = orderProcessService.processRequestedOrder(nonMemberOrderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping("/{order-id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable("order-id") String orderId) {
        orderProcessService.completeOrder(orderId);

        return ResponseEntity.ok(orderId);
    }
}
