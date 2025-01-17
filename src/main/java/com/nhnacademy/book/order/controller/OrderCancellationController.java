package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.service.OrderCancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderCancellationController {
    private final OrderCancellationService orderCancellationService;


    /**
     * 주문취소
     *
     * @param orderId 주문아이디
     * @param cancelRequest 주문취소 DTO
     * @return 상태코드
     */
    @PostMapping("/{order-id}/cancel")
    public ResponseEntity<Void> cancelOrderProduct(@PathVariable("order-id") String orderId,
                                                   @RequestBody OrderCancelRequestDto cancelRequest) {
        orderCancellationService.cancelOrderProducts(orderId, cancelRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
