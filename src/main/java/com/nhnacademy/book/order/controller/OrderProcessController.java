package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.service.OrderProcessService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> requestOrder(@RequestHeader(name = "X-USER-ID", required = false) String memberEmail,
                                                         @Valid @RequestBody OrderRequestDto orderRequest) {

        if (memberEmail != null) {
            orderRequest.setMemberEmail(memberEmail);
        }

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
