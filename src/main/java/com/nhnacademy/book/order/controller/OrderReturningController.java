package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnRequestDto;
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
public class OrderReturningController {
    private final OrderReturningService orderReturningService;

    @GetMapping("/order-returns")
    public ResponseEntity<Page<OrderReturnDto>> getOrderReturns(@ModelAttribute OrderReturnSearchRequestDto searchRequest,
                                                                Pageable pageable) {
        Page<OrderReturnDto> orderReturnDtoPage = orderReturningService.getAllOrderReturns(searchRequest, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(orderReturnDtoPage);
    }

    /**
     * 주문반품 요청
     *
     * @param orderId 반품할 주문 아이디
     * @param orderProductReturnRequest 주문반품요청 DTO
     * @return
     */
//    @PostMapping("/{order-id}/return")
//    public ResponseEntity<?> requestReturnOrder(@PathVariable("order-id") String orderId,
//                                                @Valid @RequestBody OrderReturnRequestDto refundRequest) {
//        orderReturningService.requestOrderReturn(orderId, refundRequest);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @PostMapping("/{order-id}/order-products/{order-product-id}/return")
    public ResponseEntity<?> requestReturnOrderProduct(@PathVariable("order-id") String orderId,
                                                       @PathVariable("order-product-id") Long orderProductId,
                                                       @Valid @RequestBody OrderProductReturnRequestDto orderProductReturnRequest) {
        orderReturningService.requestOrderProductReturn(orderId, orderProductId, orderProductReturnRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 주문반품요청 완료처리
     *
     * @param orderId
     * @return
     */
//    @PostMapping("/{order-id}/return/complete")
//    public ResponseEntity<?> completeReturnOrder(@PathVariable("order-id") String orderId) {
//        orderReturningService.completeOrderReturn(orderId);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @PostMapping("/{order-id}/order-products/{order-product-id}/return/complete")
    public ResponseEntity<?> completeReturnOrderProduct(@PathVariable("order-id") String orderId,
                                                        @PathVariable("order-product-id") Long orderProductId) {
        orderReturningService.completeOrderProductReturn(orderProductId);
        return ResponseEntity.status(HttpStatus.OK).body(orderId);
    }
}
