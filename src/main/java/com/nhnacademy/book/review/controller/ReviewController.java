package com.nhnacademy.book.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.objectStorage.service.ObjectStorageService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewWithReviewImageDto;
import com.nhnacademy.book.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderProductService orderProductService;
    private final ObjectStorageService objectStorageService;

    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestPart("reviewRequestDto") String reviewRequestDtoJson,  // String으로 받아 JSON을 처리
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        // 받은 JSON 데이터를 ReviewCreateRequestDto 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewCreateRequestDto reviewRequestDto;
        try {
            reviewRequestDto = objectMapper.readValue(reviewRequestDtoJson, ReviewCreateRequestDto.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);  // JSON 파싱 실패 시 오류 반환
        }

        List<String> imageUrls = new ArrayList<>();
        // 이미지가 첨부된 경우만 처리
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {  // 비어 있는 파일은 건너뛰기
                    String imageUrl = saveImage(image);  // 이미지를 저장하고 URL 반환
                    imageUrls.add(imageUrl);
                }
            }
        }


        // 리뷰 생성 서비스 호출
        ReviewResponseDto reviewResponseDto = reviewService.createReview(reviewRequestDto, imageUrls);
        return ResponseEntity.ok(reviewResponseDto);  // 성공적으로 생성된 리뷰 반환
    }

    private String saveImage(MultipartFile image) {
        return objectStorageService.uploadObjects(List.of(image)).get(0);
    }

    @GetMapping("/order-product/by-selling-book/{sellingBookId}")
    public ResponseEntity<Long> getOrderProductBySellingBookId(@PathVariable("sellingBookId") Long sellingBookId) {
        return orderProductService.findOrderProductBySellingBookId(sellingBookId)
                .map(orderProduct -> ResponseEntity.ok(orderProduct.getOrderProductId()))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/books/{sellingBookId}/reviews")
    public ResponseEntity<List<ReviewWithReviewImageDto>> getReviewsByBookId(@PathVariable("sellingBookId") Long sellingBookId) {
        List<ReviewWithReviewImageDto> review = reviewService.getReviewsWithReviewImagesByBookId(sellingBookId);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/books/{sellingBookId}/reviews/avg")
    public Double getAverageReview(@PathVariable("sellingBookId") Long sellingBookId) {
        return reviewService.averageRatingByBookId(sellingBookId);
    }
}
