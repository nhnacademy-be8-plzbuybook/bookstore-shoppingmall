package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.service.OrderReturningService;
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
public class OrderReturnController {
    private final OrderReturningService orderReturningService;

    @GetMapping("/order-product-returns")
    public ResponseEntity<Page<OrderProductReturnDto>> getOrderReturns(@ModelAttribute OrderReturnSearchRequestDto searchRequest,
                                                                       Pageable pageable) {
        Page<OrderProductReturnDto> orderProductReturnDtoPage = orderReturningService.getAllOrderProductReturns(searchRequest, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(orderProductReturnDtoPage);
    }


    @PostMapping("/{order-id}/order-products/{order-product-id}/return")
    public ResponseEntity<?> requestReturnOrderProduct(@PathVariable("order-id") String orderId,
                                                       @PathVariable("order-product-id") Long orderProductId,
                                                       @Valid @RequestBody OrderProductReturnRequestDto orderProductReturnRequest) {
        orderReturningService.requestOrderProductReturn(orderId, orderProductId, orderProductReturnRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{order-id}/order-products/{order-product-id}/return/complete")
    public ResponseEntity<?> completeReturnOrderProduct(@PathVariable("order-id") String orderId,
                                                        @PathVariable("order-product-id") Long orderProductId) {
        orderReturningService.completeOrderProductReturn(orderProductId);
        return ResponseEntity.status(HttpStatus.OK).body(orderId);
    }
}
