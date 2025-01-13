package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.NonMemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.service.OrderProcessService;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
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
    private final OrderProductService orderProductService;


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

    /**
     * 전체 주문취소
     *
     * @param orderId
     * @return
     */
    @PostMapping("/{order-id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("order-id") String orderId,
                                         @RequestBody OrderCancelRequestDto cancelRequest) {
        orderProcessService.cancelOrder(orderId, cancelRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 주문상품 취소
     *
     * @param orderProductId 주문상품 아이디
     * @param quantity       취소할 수량
     * @return
     */
    @PostMapping("/order-products/{order-product-id}/cancel")
    public ResponseEntity<?> cancelOrderProduct(@PathVariable("order-product-id") Long orderProductId,
                                                @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity) {
        orderProductService.cancelOrderProduct(orderProductId, quantity);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 주문반품 요청
     *
     * @param orderId 반품할 주문 아이디
     * @param refundRequest 주문반품요청 DTO
     * @return
     */
    @PostMapping("/{order-id}/return")
    public ResponseEntity<?> requestRefundOrder(@PathVariable("order-id") String orderId,
                                                @Valid @RequestBody OrderReturnRequestDto refundRequest) {
        orderProcessService.requestOrderReturn(orderId, refundRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 주문반품요청 완료
     *
     * @param orderId
     * @return
     */
    @PostMapping("/{order-id}/return/complete")
    public ResponseEntity<?> approveRefundOrder(@PathVariable("order-id") String orderId) {
        orderProcessService.completeOrderReturn(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
