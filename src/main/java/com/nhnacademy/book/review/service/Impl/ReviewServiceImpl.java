package com.nhnacademy.book.review.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.objectstorage.service.ObjectStorageService;
import com.nhnacademy.book.order.repository.MemberOrderRepository;
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
import com.nhnacademy.book.review.exception.DuplicateReviewException;
import com.nhnacademy.book.review.exception.InvalidOrderAccessException;
import com.nhnacademy.book.review.exception.InvalidOrderProductStatusException;
import com.nhnacademy.book.review.exception.ReviewNotFoundException;
import com.nhnacademy.book.review.repository.ReviewImageRepository;
import com.nhnacademy.book.review.repository.ReviewRepository;
import com.nhnacademy.book.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MemberRepository memberRepository;
    private final OrderProductRepository orderProductRepository;
    private final ReviewRepository reviewRepository;
    private final MemberOrderRepository memberOrderRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ObjectStorageService objectStorageService;
    private final MemberPointService memberPointService;

    @Override
    public ReviewResponseDto createReview(ReviewCreateRequestDto requestDto, List<String> imageUrls) {
        //sellingBookId와 회원 ID로 구매 확정된 상품 조회
        List<OrderProduct> orderProducts = orderProductRepository.findBySellingBookIdANdMemberId(
                requestDto.getSellingBookId(),
                requestDto.getMemberId()
        );

        if(orderProducts.isEmpty()){
            throw new InvalidOrderAccessException("구매 확정된 상품만 리뷰를 작성할 수 있다!");
        }

        OrderProduct confirmedOrderProduct = null;
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getStatus().equals(OrderProductStatus.PURCHASE_CONFIRMED)) {
                confirmedOrderProduct = orderProduct;
                break;
            }
        }

        if (confirmedOrderProduct == null) {
            throw new InvalidOrderProductStatusException("구매 확정된 상품만 리뷰를 작성할 수 있다!");
        }

        //Order_Product에서 상태가 구매 확정인 것만 리뷰를 작성할 수 있다
        if(reviewRepository.existsByOrderProduct(confirmedOrderProduct)){
            throw new DuplicateReviewException("이미 리뷰가 작성됨!");
        }

        Member member = memberRepository.findById(requestDto.getMemberId()).get();

        Review review = new Review(
                member,
                confirmedOrderProduct,
                requestDto.getScore(),
                requestDto.getContent(),
                false
        );

        Review savedReview = reviewRepository.save(review);

        if(imageUrls != null && !imageUrls.isEmpty()){
            for(String imageUrl : imageUrls){
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(savedReview);
                String id = objectStorageService.getUrl(imageUrl);
                reviewImage.setReviewImageUrl(id);
                review.setPhotoPointGiven(true);
                reviewImageRepository.save(reviewImage);
            }
        }

        memberPointService.addReviewPoint(review);
        return new ReviewResponseDto(
                savedReview.getReviewId(),
                savedReview.getMember().getMemberId(),
                savedReview.getOrderProduct().getOrderProductId(),
                savedReview.getScore(),
                savedReview.getContent()
        );
    }


    @Override
    public Page<ReviewWithReviewImageDto> getReviewsWithReviewImagesByBookId(Long bookId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findPagingReviewByBookId(bookId, pageable);

        List<Long> reviewIds = reviewPage.getContent().stream()
                .map(Review::getReviewId)
                .toList();

        List<ReviewImage> reviewImages = reviewImageRepository.findImagesByBookIdAndReviewIds(bookId, reviewIds);

        Map<Long, List<String>> reviewImageMap = reviewImages.stream()
                .collect(Collectors.groupingBy(
                        image -> image.getReview().getReviewId(),
                        Collectors.mapping(ReviewImage::getReviewImageUrl, Collectors.toList())
                ));

        return reviewPage.map(review -> {
            List<String> imageUrls = reviewImageMap.getOrDefault(review.getReviewId(), List.of());
            return new ReviewWithReviewImageDto(
                    review.getMember().getMemberId(),
                    review.getReviewId(),
                    review.getMember().getEmail(),
                    review.getOrderProduct().getOrderProductId(),
                    review.getWriteDate(),
                    review.getScore(),
                    review.getContent(),
                    imageUrls
            );
        });
    }

    @Override
    public Double averageRatingByBookId(Long BookId) {
        List<Review> reviewList = reviewRepository.findReviewByBookId(BookId);

        if(reviewList.isEmpty()){
            return 0.0;
        }

        double totalRating = reviewList.stream()
                .mapToDouble(Review::getScore)
                .sum();

        return totalRating/reviewList.size();
    }

    @Override
    public void updateReview(Long reviewId, ReviewUpdateRequestDto updateRequestDto, List<String> imageUrls) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없다!"));

        review.setScore(updateRequestDto.getScore());
        review.setContent(updateRequestDto.getContent());
        review.setWriteDate(LocalDateTime.now());

        //기존 이미지 삭제
        reviewImageRepository.deleteByReview(review);

        boolean isPhotoAdded = false;

        if(imageUrls != null && !imageUrls.isEmpty()){
            for (String imageUrl : imageUrls) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(review);
                String id = objectStorageService.getUrl(imageUrl);
                reviewImage.setReviewImageUrl(id);
                reviewImageRepository.save(reviewImage);
            }
            isPhotoAdded = true;
        }

        if(isPhotoAdded && !review.isPhotoPointGiven()){
            memberPointService.updatePointForReview(review, true);
        } else if(!isPhotoAdded && review.isPhotoPointGiven()){
            memberPointService.updatePointForReview(review, false);
        }
        reviewRepository.save(review);


    }
}
