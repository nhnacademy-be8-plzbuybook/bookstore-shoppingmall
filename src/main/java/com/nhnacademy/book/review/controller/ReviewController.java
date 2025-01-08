package com.nhnacademy.book.review.controller;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderProductService orderProductService;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewCreateRequestDto reviewRequestDto) {
        ReviewResponseDto reviewResponseDto = reviewService.createReview(reviewRequestDto);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @GetMapping("/order-product/by-selling-book/{sellingBookId}")
    public ResponseEntity<Long> getOrderProductBySellingBookId(@PathVariable("sellingBookId") Long sellingBookId) {
        return orderProductService.findOrderProductBySellingBookId(sellingBookId)
                .map(orderProduct -> ResponseEntity.ok(orderProduct.getOrderProductId()))
                .orElse(ResponseEntity.notFound().build());
    }
}
