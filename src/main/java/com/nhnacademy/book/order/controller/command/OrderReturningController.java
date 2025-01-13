package com.nhnacademy.book.order.controller.command;

import com.nhnacademy.book.order.dto.OrderReturnRequestDto;
import com.nhnacademy.book.order.service.OrderProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderReturningController {
    private final OrderProcessService orderProcessService;


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
        orderProcessService.requestOrderReturn(orderId, refundRequest);
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
        orderProcessService.completeOrderReturn(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
