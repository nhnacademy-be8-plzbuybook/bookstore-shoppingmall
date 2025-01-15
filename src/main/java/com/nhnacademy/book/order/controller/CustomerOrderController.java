package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.service.OrderProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class CustomerOrderController {
    private final MemberService memberService;
    private final OrderProcessService orderProcessService;

    /**
     * 회원 주문요청
     *
     * @param memberEmail        액세스토큰에서 추출된 X-USER-ID
     * @param memberOrderRequest 주문요청 DTO
     * @return 결제정보가 포함된 주문응답 DTO
     */
    @PostMapping("/member")
    public ResponseEntity<OrderResponseDto> requestMemberOrder(@RequestHeader("X-USER-ID") String memberEmail,
                                                               @Valid @RequestBody MemberOrderRequestDto memberOrderRequest) {
        // 회원 검증
        if (memberEmail == null || memberEmail.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (memberService.getMemberByEmail(memberEmail) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        memberOrderRequest.setMemberEmail(memberEmail);

        OrderResponseDto orderResponse = orderProcessService.processRequestedOrder(memberOrderRequest);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }


    /**
     * 비회원 주문요청
     *
     * @param nonMemberOrderRequest 주문요청 DTO
     * @return 결제정보가 포함된 주문응답 DTO
     */
    @PostMapping("/non-member")
    public ResponseEntity<OrderResponseDto> requestNonMemberOrder(@Valid @RequestBody NonMemberOrderRequestDto nonMemberOrderRequest) {
        OrderResponseDto orderResponse = orderProcessService.processRequestedOrder(nonMemberOrderRequest);

        return ResponseEntity.ok(orderResponse);
    }
}
