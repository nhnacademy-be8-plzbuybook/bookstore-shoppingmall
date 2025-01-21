package com.nhnacademy.book.review.service.Impl;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.objectstorage.service.ObjectStorageService;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import com.nhnacademy.book.review.domain.Review;
import com.nhnacademy.book.review.domain.ReviewImage;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;
import com.nhnacademy.book.review.dto.ReviewWithReviewImageDto;
import com.nhnacademy.book.review.exception.*;
import com.nhnacademy.book.review.repository.ReviewImageRepository;
import com.nhnacademy.book.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)  // 이 설정을 추가하여 lenient 모드로 변경

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberPointService memberPointService;

    @Mock
    private ObjectStorageService objectStorageService;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    @DisplayName("리뷰 생성 성공 테스트 (이미지 null)")
    void createReview_success() {
        Long memberId = 1L;
        Long sellingBookId = 1L;
        Long orderProductId = 1L;
        String orderId = "testtest";

        ReviewCreateRequestDto reviewCreateRequestDto = new ReviewCreateRequestDto(
                1L,
                1L,
                5,
                "Test"
        );

        Publisher publisher = new Publisher(1L, "테스트 출판사", new ArrayList<>());

        Book book = new Book(
                publisher,
                "윤지호책",
                "윤지호Index",
                "윤지호설명",
                LocalDate.now(),
                new BigDecimal("20000"),
                "1111111111111"
        );

        SellingBook sellingBook = new SellingBook(
                sellingBookId,
                book,
                new BigDecimal("30000"),
                true,
                10,
                SellingBook.SellingBookStatus.SELLING,
                false,
                100L
        );

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");

        Orders order = Orders.builder()
                .id(orderId)
                .number("ORD-20250120")
                .name("윤지호")
                .deliveryWishDate(LocalDate.now().plusDays(3))
                .usedPoint(500)
                .deliveryFee(new BigDecimal("2500"))
                .orderedAt(LocalDateTime.now())
                .status(OrderStatus.DELIVERED)
                .orderPrice(new BigDecimal("32500"))
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .id(orderProductId)
                .sellingBook(sellingBook)
                .order(order)
                .price(new BigDecimal("30000"))
                .quantity(1)
                .status(OrderProductStatus.PURCHASE_CONFIRMED)
                .build();

        List<OrderProduct> mockOrderProducts = new ArrayList<>();
        mockOrderProducts.add(orderProduct);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(orderProductRepository.findBySellingBookIdANdMemberId(
                reviewCreateRequestDto.getSellingBookId(),
                reviewCreateRequestDto.getMemberId()
        )).thenReturn(mockOrderProducts);
        when(reviewRepository.existsByOrderProduct(orderProduct)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        doNothing().when(memberPointService).addReviewPoint(any(Review.class));

        ReviewResponseDto reviewResponseDto = reviewService.createReview(reviewCreateRequestDto, null);

        assertNotNull(reviewResponseDto);
        assertEquals(memberId, reviewResponseDto.getMemberId());
        assertEquals(orderProduct.getOrderProductId(), reviewResponseDto.getOrderProductId());
        assertEquals(5, reviewResponseDto.getScore());
        assertEquals("Test", reviewResponseDto.getContent());

        verify(memberRepository, times(1)).findById(memberId);
        verify(orderProductRepository, times(1)).findBySellingBookIdANdMemberId(sellingBookId, memberId);
        verify(reviewRepository, times(1)).existsByOrderProduct(orderProduct);
        verify(reviewRepository, times(1)).save(any(Review.class));

    }

    @Test
    @DisplayName("리뷰 생성 성공 테스트 (이미지 URL 포함)")
    void createReview_withImages_success() {
        Long memberId = 1L;
        Long sellingBookId = 1L;
        Long orderProductId = 1L;
        String orderId = "testtest";

        ReviewCreateRequestDto reviewCreateRequestDto = new ReviewCreateRequestDto(
                1L,
                1L,
                5,
                "Test"
        );

        Publisher publisher = new Publisher(1L, "테스트 출판사", new ArrayList<>());

        Book book = new Book(
                publisher,
                "윤지호책",
                "윤지호Index",
                "윤지호설명",
                LocalDate.now(),
                new BigDecimal("20000"),
                "1111111111111"
        );

        SellingBook sellingBook = new SellingBook(
                sellingBookId,
                book,
                new BigDecimal("30000"),
                true,
                10,
                SellingBook.SellingBookStatus.SELLING,
                false,
                100L
        );

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");

        Orders order = Orders.builder()
                .id(orderId)
                .number("ORD-20250120")
                .name("윤지호")
                .deliveryWishDate(LocalDate.now().plusDays(3))
                .usedPoint(500)
                .deliveryFee(new BigDecimal("2500"))
                .orderedAt(LocalDateTime.now())
                .status(OrderStatus.DELIVERED)
                .orderPrice(new BigDecimal("32500"))
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .id(orderProductId)
                .sellingBook(sellingBook)
                .order(order)
                .price(new BigDecimal("30000"))
                .quantity(1)
                .status(OrderProductStatus.PURCHASE_CONFIRMED)
                .build();

        List<OrderProduct> mockOrderProducts = new ArrayList<>();
        mockOrderProducts.add(orderProduct);

        List<String> imageUrls = List.of("imageUrl1", "imageUrl2");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(orderProductRepository.findBySellingBookIdANdMemberId(
                reviewCreateRequestDto.getSellingBookId(),
                reviewCreateRequestDto.getMemberId()
        )).thenReturn(mockOrderProducts);
        when(reviewRepository.existsByOrderProduct(orderProduct)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // memberPointService의 addReviewPoint 메소드 호출 시 아무 동작도 하지 않도록 설정
        doNothing().when(memberPointService).addReviewPoint(any(Review.class));

        when(objectStorageService.getUrl(anyString())).thenReturn("mockUrl");

        ReviewResponseDto reviewResponseDto = reviewService.createReview(reviewCreateRequestDto, imageUrls);

        assertNotNull(reviewResponseDto);
        assertEquals(memberId, reviewResponseDto.getMemberId());
        assertEquals(orderProduct.getOrderProductId(), reviewResponseDto.getOrderProductId());
        assertEquals(5, reviewResponseDto.getScore());
        assertEquals("Test", reviewResponseDto.getContent());

        verify(memberRepository, times(1)).findById(memberId);
        verify(orderProductRepository, times(1)).findBySellingBookIdANdMemberId(sellingBookId, memberId);
        verify(reviewRepository, times(1)).existsByOrderProduct(orderProduct);
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(objectStorageService, times(2)).getUrl(anyString()); // getUrl 호출 확인
        verify(reviewImageRepository, times(2)).save(any(ReviewImage.class)); // ReviewImage 저장 확인
    }


    @Test
    @DisplayName("책 ID로 리뷰와 리뷰 이미지를 페이징 조회 테스트")
    void getReviewsWithReviewImagesByBookIdTest() {
        Long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Publisher publisher = new Publisher(1L, "테스트 출판사", new ArrayList<>());

        Book book = new Book(
                publisher,
                "윤지호책",
                "윤지호Index",
                "윤지호설명",
                LocalDate.now(),
                new BigDecimal("20000"),
                "1111111111111"
        );

        SellingBook sellingBook = new SellingBook(
                1L,
                book,
                new BigDecimal("30000"),
                true,
                10,
                SellingBook.SellingBookStatus.SELLING,
                false,
                100L
        );

        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");
        Member member2 = new Member(2L, memberGrade, memberStatus, "윤지호2", "010-7237-3952", "yoonwlgh123@naver.com", LocalDate.now(), "encodedPassword");

        Orders order = Orders.builder()
                .id("testtest")
                .number("ORD-20250120")
                .name("윤지호")
                .deliveryWishDate(LocalDate.now().plusDays(3))
                .usedPoint(500)
                .deliveryFee(new BigDecimal("2500"))
                .orderedAt(LocalDateTime.now())
                .status(OrderStatus.DELIVERED)
                .orderPrice(new BigDecimal("32500"))
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .id(1L)
                .sellingBook(sellingBook)
                .order(order)
                .price(new BigDecimal("30000"))
                .quantity(1)
                .status(OrderProductStatus.PURCHASE_CONFIRMED)
                .build();

        OrderProduct orderProduct2 = OrderProduct.builder()
                .id(2L)
                .sellingBook(sellingBook)
                .order(order)
                .price(new BigDecimal("30000"))
                .quantity(1)
                .status(OrderProductStatus.PURCHASE_CONFIRMED)
                .build();

        Review review1 = new Review(member, orderProduct, 5, "Good book!", true);
        Review review2 = new Review(member2, orderProduct2, 3, "Interesting read.", true);
        List<Review> reviews = Arrays.asList(review1, review2);
        Page<Review> reviewPage = new PageImpl<>(reviews);

        ReviewImage image1 = new ReviewImage(1L, review1, "imageUrl1");
        ReviewImage image2 = new ReviewImage(2L, review1, "imageUrl2");
        ReviewImage image3 = new ReviewImage(3L, review2, "imageUrl3");
        List<ReviewImage> reviewImages = Arrays.asList(image1, image2, image3);

        doReturn(reviewPage).when(reviewRepository).findPagingReviewByBookId(bookId, pageable);
        doReturn(reviewImages).when(reviewImageRepository).findImagesByBookIdAndReviewIds(bookId, Arrays.asList(1L, 2L));

        Page<ReviewWithReviewImageDto> result = reviewService.getReviewsWithReviewImagesByBookId(bookId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

    }

    @Test
    @DisplayName("구매 확정된 상품이 없을 때 InvalidOrderAccessException 발생")
    void createReview_InvalidOrderAccessException() {
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, 1L, 5, "좋은 책!");
        List<String> imageUrls = Arrays.asList("image1.jpg", "image2.jpg");

        when(orderProductRepository.findBySellingBookIdANdMemberId(requestDto.getSellingBookId(), requestDto.getMemberId()))
                .thenReturn(Collections.emptyList());

        assertThrows(InvalidOrderAccessException.class, () -> reviewService.createReview(requestDto, imageUrls));
    }

    @Test
    @DisplayName("구매 확정 상태의 상품이 없을 때 InvalidOrderProductStatusException 발생")
    void createReview_InvalidOrderProductStatusException() {
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, 1L, 5, "좋은 책!");
        List<String> imageUrls = Arrays.asList("image1.jpg", "image2.jpg");

        OrderProduct orderProduct = mock(OrderProduct.class);
        when(orderProduct.getStatus()).thenReturn(OrderProductStatus.DELIVERING);  // 구매 확정 상태 아님
        when(orderProductRepository.findBySellingBookIdANdMemberId(requestDto.getSellingBookId(), requestDto.getMemberId()))
                .thenReturn(Collections.singletonList(orderProduct));

        assertThrows(InvalidOrderProductStatusException.class, () -> reviewService.createReview(requestDto, imageUrls));
    }

    @Test
    @DisplayName("이미 리뷰가 작성된 상품에 대해 DuplicateReviewException 발생")
    void createReview_DuplicateReviewException() {
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(1L, 1L, 5, "좋은 책!");
        List<String> imageUrls = Arrays.asList("image1.jpg", "image2.jpg");

        OrderProduct orderProduct = mock(OrderProduct.class);
        when(orderProduct.getStatus()).thenReturn(OrderProductStatus.PURCHASE_CONFIRMED);  // 구매 확정 상태
        when(orderProductRepository.findBySellingBookIdANdMemberId(requestDto.getSellingBookId(), requestDto.getMemberId()))
                .thenReturn(Collections.singletonList(orderProduct));

        when(reviewRepository.existsByOrderProduct(orderProduct)).thenReturn(true);

        assertThrows(DuplicateReviewException.class, () -> reviewService.createReview(requestDto, imageUrls));
    }

    @Test
    @DisplayName("리뷰가 없을 때 평균 평점 0.0")
    void averageRatingByBookIt_EmptyReviews() {
        Long bookId = 1L;
        when(reviewRepository.findReviewByBookId(bookId)).thenReturn(Collections.emptyList());

        Double result = reviewService.averageRatingByBookId(bookId);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("리뷰가 있을 때 평균 평점 계산")
    void averageRatingByBookIt_Reviews() {
        Long bookId = 1L;
        Review review1 = new Review();
        review1.setScore(5);
        Review review2 = new Review();
        review2.setScore(3);
        List<Review> reviews = Arrays.asList(review1, review2);
        when(reviewRepository.findReviewByBookId(bookId)).thenReturn(reviews);

        Double result = reviewService.averageRatingByBookId(bookId);
        assertEquals(4.0, result);
    }

    @Test
    @DisplayName("리뷰가 없을 경우 예외를 발생시킨다.")
    void updateReview_ReviewNotFound() {
        // Given
        Long reviewId = 1L;
        ReviewUpdateRequestDto updateRequestDto = new ReviewUpdateRequestDto(4, "Updated content");
        List<String> imageUrls = Arrays.asList("imageUrl1", "imageUrl2");

        // Mock: 리뷰가 존재하지 않는 경우
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReview(reviewId, updateRequestDto, imageUrls));
    }

    @Test
    @DisplayName("리뷰를 정상적으로 업데이트하고 이미지 추가 시 포인트가 갱신된다.")
    void updateReview_Success_WithImages() {
        // Given
        Long reviewId = 1L;
        ReviewUpdateRequestDto updateRequestDto = new ReviewUpdateRequestDto(4, "Updated content");
        List<String> imageUrls = Arrays.asList("imageUrl1", "imageUrl2");

        // 리뷰가 존재하고, 기존 이미지가 있다고 가정
        Review existingReview = new Review();
        existingReview.setScore(5);
        existingReview.setContent("Old content");
        existingReview.setPhotoPointGiven(false); // 이전에 포인트를 받지 않은 상태
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        // 이미지 저장을 위한 service 메서드
        when(objectStorageService.getUrl(anyString())).thenReturn("url");

        // When
        reviewService.updateReview(reviewId, updateRequestDto, imageUrls);

        // Then
        assertEquals(4, existingReview.getScore()); // 평점 업데이트 확인
        assertEquals("Updated content", existingReview.getContent()); // 내용 업데이트 확인

        verify(reviewImageRepository, times(2)).save(any(ReviewImage.class)); // 이미지가 두 개 추가됨
        verify(memberPointService, times(1)).updatePointForReview(existingReview, true); // 포인트가 갱신됨
    }

    @Test
    @DisplayName("이미지가 없을 경우 기존 포인트를 제거한다.")
    void updateReview_NoImages_RemovePhotoPoint() {
        Long reviewId = 1L;
        ReviewUpdateRequestDto updateRequestDto = new ReviewUpdateRequestDto(4, "Updated content");
        List<String> imageUrls = Collections.emptyList(); // 이미지 없음

        // 리뷰가 존재하고, 기존에 이미지가 있어 포인트가 주어진 상태
        Review existingReview = new Review();
        existingReview.setScore(5);
        existingReview.setContent("Old content");
        existingReview.setPhotoPointGiven(true); // 포인트가 주어진 상태
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        reviewService.updateReview(reviewId, updateRequestDto, imageUrls);

        assertEquals(4, existingReview.getScore()); // 평점 업데이트 확인
        assertEquals("Updated content", existingReview.getContent()); // 내용 업데이트 확인

        verify(reviewImageRepository, times(0)).save(any(ReviewImage.class)); // 이미지가 없으므로 이미지 저장 안 됨
        verify(memberPointService, times(1)).updatePointForReview(existingReview, false); // 포인트 제거
    }
}
