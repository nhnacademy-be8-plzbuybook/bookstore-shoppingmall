package com.nhnacademy.book.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.objectStorage.service.ObjectStorageService;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;
import com.nhnacademy.book.review.dto.ReviewWithReviewImageDto;
import com.nhnacademy.book.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private OrderProductService orderProductService;

    @MockBean
    private ObjectStorageService objectStorageService;

    @Test
    void createReview_Success() throws Exception {
        // Mock 데이터 준비
        Long reviewId = 1L;
        Long memberId = 1L;
        Long orderProductId = 10L;
        String content = "Great book!";
        int score = 5;
        ReviewResponseDto mockResponse = new ReviewResponseDto(reviewId, memberId, orderProductId, score, content);

        // ReviewCreateRequestDto 객체를 JSON 문자열로 변환
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(memberId, orderProductId, score, content);
        ObjectMapper objectMapper = new ObjectMapper();
        String reviewRequestDtoJson = objectMapper.writeValueAsString(requestDto);

        // Mock MultipartFile 이미지 생성
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1-content".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", MediaType.IMAGE_PNG_VALUE, "image2-content".getBytes());

        // Mock 서비스 동작 정의
        given(objectStorageService.uploadObjects(anyList()))
                .willReturn(List.of("http://image-url1.com", "http://image-url2.com"));
        given(reviewService.createReview(any(ReviewCreateRequestDto.class), anyList()))
                .willReturn(mockResponse);

        // 테스트 실행
        mockMvc.perform(multipart("/api/reviews")
                        .file(new MockMultipartFile("reviewRequestDto", "", MediaType.APPLICATION_JSON_VALUE, reviewRequestDtoJson.getBytes())) // JSON part
                        .file(image1) // 이미지 part
                        .file(image2) // 추가 이미지 part
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(reviewId))
                .andExpect(jsonPath("$.orderProductId").value(orderProductId))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.score").value(score));

        // Mock 검증
        then(reviewService).should().createReview(any(ReviewCreateRequestDto.class), anyList());
    }


    @Test
    public void getOrderProductBySellingBookId_Success() throws Exception {
        Long sellingBookId = 1L;
        Long expectedOrderProductId = 123L;

        // SellingBook과 Orders 생성
        Book book = new Book();
        book.setBookTitle("Example Book");

        SellingBook sellingBook = new SellingBook(
                sellingBookId
                ,book
                ,BigDecimal.valueOf(25000)
                ,true
                ,100
                ,SellingBook.SellingBookStatus.SELLING
                ,false
                ,0L
                );

        Orders order = Orders.builder()
                .id("123456")
                .number("ORD123456")
                .name("John Doe")
                .orderedAt(LocalDateTime.now())
                .deliveryWishDate(LocalDate.now().plusDays(5))
                .usedPoint(0)
                .deliveryFee(BigDecimal.valueOf(2000))
                .orderPrice(BigDecimal.valueOf(25000))
                .status(OrderStatus.DELIVERED)
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .id(expectedOrderProductId)
                .sellingBook(sellingBook)
                .order(order)
                .price(sellingBook.getSellingBookPrice())
                .quantity(1)
                .couponDiscount(BigDecimal.ZERO)
                .status(OrderProductStatus.DELIVERED)
                .build();

        order.addOrderProduct(orderProduct);

        // Mock service의 반환값 설정
        given(orderProductService.findOrderProductBySellingBookId(sellingBookId))
                .willReturn(Optional.of(orderProduct));

        // 테스트 실행
        mockMvc.perform(get("/api/order-product/by-selling-book/{sellingBookId}", sellingBookId))
                .andExpect(status().isOk())
                .andExpect(content().string("123"));
    }


    @Test
    void getReviewsByBookId_Success() throws Exception {
        Long sellingBookId = 1L;

        // ReviewWithReviewImageDto 객체 준비
        ReviewWithReviewImageDto reviewDto1 = new ReviewWithReviewImageDto(
                1L,
                100L,
                "user1@example.com",
                10L,
                LocalDateTime.now(),
                5,
                "Excellent book!",
                List.of("image1.jpg", "image2.jpg")
        );

        ReviewWithReviewImageDto reviewDto2 = new ReviewWithReviewImageDto(
                2L,
                101L,
                "user2@example.com",
                11L,
                LocalDateTime.now(),
                4,
                "Good read",
                List.of("image3.jpg")
        );

        // Page 객체 생성
        List<ReviewWithReviewImageDto> reviews = List.of(reviewDto1, reviewDto2);
        Page<ReviewWithReviewImageDto> reviewsPage = new PageImpl<>(reviews, PageRequest.of(0, 2), reviews.size());

        // Mock service의 반환값 설정
        given(reviewService.getReviewsWithReviewImagesByBookId(anyLong(), any(Pageable.class)))
                .willReturn(reviewsPage);

        // 테스트 실행
        mockMvc.perform(get("/api/books/{sellingBookId}/reviews", sellingBookId)
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))  // 리뷰의 수가 2개인지 확인
                .andExpect(jsonPath("$.content[0].memberId").value(1))  // 첫 번째 리뷰의 memberId가 1인지 확인
                .andExpect(jsonPath("$.content[0].reviewId").value(100))  // 첫 번째 리뷰의 reviewId가 100인지 확인
                .andExpect(jsonPath("$.content[0].email").value("user1@example.com"))  // 첫 번째 리뷰의 이메일 확인
                .andExpect(jsonPath("$.content[0].imageUrls.length()").value(2))  // 첫 번째 리뷰의 이미지가 2개인지 확인
                .andExpect(jsonPath("$.content[1].reviewId").value(101))  // 두 번째 리뷰의 reviewId가 101인지 확인
                .andExpect(jsonPath("$.content[1].imageUrls.length()").value(1));  // 두 번째 리뷰의 이미지가 1개인지 확인
    }



    @Test
    void getAverageReview_Success() throws Exception {
        Long sellingBookId = 1L;
        Double expectedAverageRating = 4.5;

        // Mock 서비스 반환값 설정
        given(reviewService.averageRatingByBookId(anyLong())).willReturn(expectedAverageRating);

        // 테스트 실행
        mockMvc.perform(get("/api/books/{sellingBookId}/reviews/avg", sellingBookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAverageRating.toString()));
    }

    @Test
    void updateReview_Success() throws Exception {
        Long reviewId = 1L;
        Integer score = 5;
        String content = "Updated review content";

        // Mock 이미지 파일 생성
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", MediaType.IMAGE_JPEG_VALUE, "image1-content".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", MediaType.IMAGE_PNG_VALUE, "image2-content".getBytes());
        MockMultipartFile contentPart = new MockMultipartFile("content", "content", MediaType.TEXT_PLAIN_VALUE, content.getBytes());

        // Mock 서비스 반환값 설정
        given(objectStorageService.uploadObjects(anyList())).willReturn(List.of("http://image-url1.com", "http://image-url2.com"));
        willDoNothing().given(reviewService).updateReview(eq(reviewId), any(ReviewUpdateRequestDto.class), anyList());

        // 테스트 실행
        mockMvc.perform(multipart("/api/reviews/{reviewId}", reviewId)
                        .file(image1)
                        .file(image2)
                        .file(contentPart) // "content" 추가
                        .param("score", score.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // 서비스 메서드 호출 검증
        then(reviewService).should().updateReview(eq(reviewId), any(ReviewUpdateRequestDto.class), anyList());
    }
}