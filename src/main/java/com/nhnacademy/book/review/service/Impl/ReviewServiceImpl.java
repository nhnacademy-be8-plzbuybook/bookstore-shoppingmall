package com.nhnacademy.book.review.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberEmailNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.objectStorage.service.ObjectStorageService;
import com.nhnacademy.book.order.entity.MemberOrder;
import com.nhnacademy.book.order.repository.MemberOrderRepository;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.review.domain.Review;
import com.nhnacademy.book.review.domain.ReviewImage;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;
import com.nhnacademy.book.review.dto.ReviewWithReviewImageDto;
import com.nhnacademy.book.review.exception.DuplicateReviewException;
import com.nhnacademy.book.review.exception.InvalidOrderAccessException;
import com.nhnacademy.book.review.exception.InvalidOrderProductStatusException;
import com.nhnacademy.book.review.exception.OrderProductNotFoundException;
import com.nhnacademy.book.review.repository.ReviewImageRepository;
import com.nhnacademy.book.review.repository.ReviewRepository;
import com.nhnacademy.book.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
                requestDto.getContent()
        );

        Review savedReview = reviewRepository.save(review);

        if(imageUrls != null && !imageUrls.isEmpty()){
            for(String imageUrl : imageUrls){
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(savedReview);
                String id = objectStorageService.getUrl(imageUrl);
                reviewImage.setReviewImageUrl(id);
                reviewImageRepository.save(reviewImage);
            }
        }

        return new ReviewResponseDto(
                savedReview.getReviewId(),
                savedReview.getMember().getMemberId(),
                savedReview.getOrderProduct().getOrderProductId(),
                savedReview.getScore(),
                savedReview.getContent()
        );
    }


    @Override
    public List<ReviewWithReviewImageDto> getReviewsWithReviewImagesByBookId(Long BookId) {
        List<Review> reviewList = reviewRepository.findReviewByBookId(BookId);
        return reviewList.stream()
                .map(review -> {
                    List<ReviewImage> reviewImageList = reviewImageRepository.findReviewImageByBookId(BookId).stream()
                            .filter(image -> image.getReview().getReviewId().equals(review.getReviewId()))
                            .collect(Collectors.toList());

                    List<String> imageUrls = reviewImageList.stream()
                            .map(ReviewImage::getReviewImageUrl)
                            .toList();

                    return new ReviewWithReviewImageDto(
                            review.getReviewId(),
                            review.getMember().getEmail(),
                            review.getOrderProduct().getOrderProductId(),
                            review.getWriteDate(),
                            review.getScore(),
                            review.getContent(),
                            imageUrls
                    );
                })
                .toList();
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
}
