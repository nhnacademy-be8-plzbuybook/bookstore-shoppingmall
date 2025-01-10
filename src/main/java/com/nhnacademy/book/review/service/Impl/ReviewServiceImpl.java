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
        //회원-주문 테이블에서 관계 검증 (회원은 여러개 주문을 할 수 있어서 리스트로 받고 거기에 대한 검증을 진행)
        List<MemberOrder> memberOrder = memberOrderRepository.findByOrder_IdAndMember_memberId(
              orderProductRepository.findById(requestDto.getOrderProductId())
                      .orElseThrow(() -> new OrderProductNotFoundException("존재하지 않는 주문 상품!"))
                      .getOrder().getId(),
                requestDto.getMemberId()
        );

        if(memberOrder.isEmpty()){
            throw new InvalidOrderAccessException("해당 주문은 회원의 주문이 아니다!");
        }

        // 어짜피 member는 다 같으니까 첫번째를 가져온다
        Member member = memberOrder.get(0).getMember();

        //Order_Product에서 상태가 구매 확정인 것만 리뷰를 작성할 수 있다
        OrderProduct orderProduct = orderProductRepository.findById(requestDto.getOrderProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 상품!"));
        if(!orderProduct.getStatus().equals(OrderProductStatus.PURCHASE_CONFIRMED)){
            throw new InvalidOrderProductStatusException("구매 확정된 상품만 리뷰를 작성할 수 있다!");
        }

        //중복 리뷰 방지
        if(reviewRepository.existsByOrderProduct(orderProduct)){
            throw new DuplicateReviewException("이미 리뷰가 작성됨!");
        }

        Review review = new Review(
                member,
                orderProduct,
                requestDto.getScore(),
                requestDto.getContent()
        );

        Review savedReview = reviewRepository.save(review);

        if(imageUrls != null){
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
