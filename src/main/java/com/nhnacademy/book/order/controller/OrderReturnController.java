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

    /**
     * 주문반품 목록조회
     *
     * @param searchRequest 주문반품 검색 DTO
     * @param pageable      페이징
     * @return 주문반품 DTO 페이지
     */
    @GetMapping("/order-product-returns")
    public ResponseEntity<Page<OrderProductReturnDto>> getOrderReturns(@ModelAttribute OrderReturnSearchRequestDto searchRequest,
                                                                       Pageable pageable) {
        Page<OrderProductReturnDto> orderProductReturnDtoPage = orderReturningService.getAllOrderProductReturns(searchRequest, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(orderProductReturnDtoPage);
    }


    /**
     * 주문상품 반품요청 (사용자)
     *
     * @param orderId                   주문 아이디
     * @param orderProductId            주문상품 아이디
     * @param orderProductReturnRequest 반품요청 DTO
     * @return 상태코드
     */
    @PostMapping("/{order-id}/order-products/{order-product-id}/return")
    public ResponseEntity<Void> requestReturnOrderProduct(@PathVariable("order-id") String orderId,
                                                          @PathVariable("order-product-id") Long orderProductId,
                                                          @Valid @RequestBody OrderProductReturnRequestDto orderProductReturnRequest) {
        orderReturningService.requestOrderProductReturn(orderId, orderProductId, orderProductReturnRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * 주문상품 반품요청 완료처리 (관리자)
     *
     * @param orderId        주문 아이디
     * @param orderProductId 주문상품 아이디
     * @return 주문 아이디
     */
    @PostMapping("/{order-id}/order-products/{order-product-id}/return/complete")
    public ResponseEntity<String> completeReturnOrderProduct(@PathVariable("order-id") String orderId,
                                                             @PathVariable("order-product-id") Long orderProductId) {
        orderReturningService.completeOrderProductReturn(orderProductId);
        return ResponseEntity.status(HttpStatus.OK).body(orderId);
    }
}
