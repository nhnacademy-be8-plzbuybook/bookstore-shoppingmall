package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.enums.OrderType;
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


    /**
     * 주문 요청
     *
     * @param memberEmail 회원 이메일
     * @param orderRequest 주문 요청 DTO
     * @return 주문요청 응답(결제정보)
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> requestOrder(@RequestHeader(name = "X-USER-ID") String memberEmail,
                                                         @Valid @RequestBody OrderRequestDto orderRequest) {
        orderRequest.setMemberEmail(memberEmail);
        orderRequest.setOrderType(OrderType.MEMBER_ORDER);
        OrderResponseDto orderResponse = orderProcessService.requestOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @PostMapping("/member")
    public ResponseEntity<OrderResponseDto> requestMemberOrder(@RequestHeader(name = "X-USER-ID") String memberEmail,
                                                         @Valid @RequestBody OrderRequestDto orderRequest) {
        orderRequest.setMemberEmail(memberEmail);
        orderRequest.setOrderType(OrderType.MEMBER_ORDER);
        OrderResponseDto orderResponse = orderProcessService.requestOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @PostMapping("/non-member")
    public ResponseEntity<OrderResponseDto> requestNonMemberOrder(@Valid @RequestBody OrderRequestDto orderRequest) {
        orderRequest.setOrderType(OrderType.NON_MEMBER_ORDER);
        OrderResponseDto orderResponse = orderProcessService.requestOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }


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
