package com.nhnacademy.book.order.controller;

import com.nhnacademy.book.order.dto.OrderDto;
import com.nhnacademy.book.order.dto.OrderSearchRequestDto;
import com.nhnacademy.book.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문조회(관리자)
     *
     * @param searchRequest 주문조회 DTO
     * @param pageable 페이지 정보
     * @return 주문목록 DTO
     */
    @GetMapping("/api/orders")
    public ResponseEntity<Page<OrderDto>> getMemberOrders(@ModelAttribute OrderSearchRequestDto searchRequest,
                                                          Pageable pageable) {
        Page<OrderDto> orders = orderService.getOrders(searchRequest, pageable);

        return ResponseEntity.ok(orders);
    }

}
