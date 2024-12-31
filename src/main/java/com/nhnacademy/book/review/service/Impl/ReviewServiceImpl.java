package com.nhnacademy.book.review.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberEmailNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.objectStorage.service.ObjectStorageService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.point.service.Impl.MemberPointServiceImpl;
import com.nhnacademy.book.point.service.MemberPointService;
import com.nhnacademy.book.review.domain.Review;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;
import com.nhnacademy.book.review.exception.OrderProductNotFoundException;
import com.nhnacademy.book.review.exception.ReviewNotFoundException;
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
//    private final ReviewImageRepository reviewImageRepository;
    private final MemberPointService memberPointService;


    @Override
    public ReviewResponseDto createReview(String memberEmail, Long orderProductId, ReviewCreateRequestDto requestDto) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberEmailNotFoundException("email에 해당하는 멤버가 없다!"));

        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new OrderProductNotFoundException("OrderProductId에 해당하는 OrderProduct가 없다!"));


        Review review = new Review();
        review.setMember(member);
        review.setOrderProduct(orderProduct);
        review.setScore(requestDto.getScore());
        review.setContent(requestDto.getContent());
        review.setWriteDate(LocalDateTime.now());
        review.setModifiedDate(null);

        Review savedReview = reviewRepository.save(review);
        // TODO: 요구사항 (리뷰를 작성하면 포인트가 적립됩니다. +200)
        // TODO: 요구사항 (리뷰를 작성하고, 사진까지 첨부하면 포인트가 추가 적립됩니다. +500)
        memberPointService.addReviewPoint(review);

        return new ReviewResponseDto(
                savedReview.getReviewId(),
                savedReview.getMember().getMemberId(),
                savedReview.getMember().getName(),
                savedReview.getOrderProduct().getOrderProductId(),
                savedReview.getScore(),
                savedReview.getContent(),
                savedReview.getWriteDate(),
                savedReview.getModifiedDate()
        );
    }

    @Override
    public List<ReviewResponseDto> getReviewsByProductId(Long productId) {
        return List.of();
    }

    @Override
    public List<ReviewResponseDto> getReviewsByMemberId(Long memberId) {
        List<Review> reviews = reviewRepository.findByMember_MemberId(memberId);
        if(reviews.isEmpty()) {
            throw new ReviewNotFoundException("해당 멤버 ID로 리뷰를 찾을 수 없습니다.");
        }
        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getReviewId(),
                        review.getMember().getMemberId(),
                        review.getMember().getName(),
                        review.getOrderProduct().getOrderProductId(),
                        review.getScore(),
                        review.getContent(),
                        review.getWriteDate(),
                        review.getModifiedDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰 ID로 리뷰를 찾을 수 없습니다."));

        return new ReviewResponseDto(
                review.getReviewId(),
                review.getMember().getMemberId(),
                review.getMember().getName(),
                review.getOrderProduct().getOrderProductId(),
                review.getScore(),
                review.getContent(),
                review.getWriteDate(),
                review.getModifiedDate()
        );
    }

    @Override
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰 ID로 리뷰를 찾을 수 없습니다."));

        review.setScore(reviewUpdateRequestDto.getScore());
        review.setContent(reviewUpdateRequestDto.getContent());
        review.setModifiedDate(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(
                savedReview.getReviewId(),
                savedReview.getMember().getMemberId(),
                savedReview.getMember().getName(),
                savedReview.getOrderProduct().getOrderProductId(),
                savedReview.getScore(),
                savedReview.getContent(),
                savedReview.getWriteDate(),
                savedReview.getModifiedDate()
        );
    }

    @Override
    public Double calculateAverageScoreByProductId(Long productId) {
        return 0.0;
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getReviewId(),
                        review.getMember().getMemberId(),
                        review.getMember().getName(),
                        review.getOrderProduct().getOrderProductId(),
                        review.getScore(),
                        review.getContent(),
                        review.getWriteDate(),
                        review.getModifiedDate()
                ))
                .collect(Collectors.toList());
    }
}
