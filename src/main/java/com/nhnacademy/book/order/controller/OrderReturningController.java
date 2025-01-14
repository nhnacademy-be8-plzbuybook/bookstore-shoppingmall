package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnRequestDto;
import com.nhnacademy.book.order.service.command.OrderReturningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderReturningController {
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



    /**
     * 주문반품 요청
     *
     * @param orderId 반품할 주문 아이디
     * @param refundRequest 주문반품요청 DTO
     * @return
     */
    @PostMapping("/{order-id}/return")
    public ResponseEntity<?> requestReturnOrder(@PathVariable("order-id") String orderId,
                                                @Valid @RequestBody OrderReturnRequestDto refundRequest) {
        orderReturningService.requestOrderReturn(orderId, refundRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 주문반품요청 완료처리
     *
     * @param orderId
     * @return
     */
    @PostMapping("/{order-id}/return/complete")
    public ResponseEntity<?> completeReturnOrder(@PathVariable("order-id") String orderId) {
        orderReturningService.completeOrderReturn(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
