package com.nhnacademy.book.review.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberEmailNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.review.domain.Review;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.dto.ReviewUpdateRequestDto;
import com.nhnacademy.book.review.exception.OrderProductNotFoundException;
import com.nhnacademy.book.review.repository.ReviewRepository;
import com.nhnacademy.book.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MemberRepository memberRepository;
    private final OrderProductRepository orderProductRepository;
    private final ReviewRepository reviewRepository;

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
        return List.of();
    }

    @Override
    public ReviewResponseDto getReviewById(Long reviewId) {
        return null;
    }

    @Override
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) {
        return null;
    }

    @Override
    public Double calculateAverageScoreByProductId(Long productId) {
        return 0.0;
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        return List.of();
    }
}
