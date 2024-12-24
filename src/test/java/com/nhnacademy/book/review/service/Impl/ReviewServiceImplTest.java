package com.nhnacademy.book.review.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.OrderProduct;
import com.nhnacademy.book.member.domain.exception.MemberEmailNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberIdNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.OrderProductRepository;
import com.nhnacademy.book.review.domain.Review;
import com.nhnacademy.book.review.dto.ReviewCreateRequestDto;
import com.nhnacademy.book.review.dto.ReviewResponseDto;
import com.nhnacademy.book.review.exception.OrderProductNotFoundException;
import com.nhnacademy.book.review.repository.ReviewRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//TODO orderProduct 완성된 후 수정될 예정
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;


    @Test
    @DisplayName("리뷰를 성공적으로 생성")
    void createReview_success() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"Password");

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(1L);

        Review review = new Review();
        review.setReviewId(1L);
        review.setMember(member);
        review.setOrderProduct(orderProduct);
        review.setScore(5);
        review.setContent("good");
        review.setWriteDate(LocalDateTime.now());
        review.setModifiedDate(null);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(orderProductRepository.findById(orderProduct.getOrderProductId())).thenReturn(Optional.of(orderProduct));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewCreateRequestDto request = new ReviewCreateRequestDto();
        request.setScore(5);
        request.setContent("good");

        ReviewResponseDto responseDto = reviewService.createReview(member.getEmail(), orderProduct.getOrderProductId(), request);

        assertAll(
                () -> assertNotNull(responseDto),
                () -> assertEquals(responseDto.getReviewId(), review.getReviewId()),
                () -> assertEquals(responseDto.getMemberId(), member.getMemberId()),
                () -> assertEquals(responseDto.getOrderProductId(), orderProduct.getOrderProductId())
        );

        verify(memberRepository).findByEmail(member.getEmail());
        verify(orderProductRepository).findById(orderProduct.getOrderProductId());
        verify(reviewRepository).save(any(Review.class));
    }


    @Test
    @DisplayName("리뷰를 생성하는 데 member를 못가져오는 예외")
    void createReview_fail_memberId_not_found() {
        String email = "test@naver.com";
        Long orderProductId = 1L;

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(MemberEmailNotFoundException.class,
                () -> reviewService.createReview(email, orderProductId, new ReviewCreateRequestDto())
        );

        verify(memberRepository).findByEmail(email);
        verify(orderProductRepository,never()).findById(anyLong());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("리뷰를 생성하는데 OrderProduct를 못가져오는 예외")
    void createReview_fail_orderProductId_not_found() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(),"Password");

        Long orderProductId = 1L;

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.empty());

        assertThrows(OrderProductNotFoundException.class,
                () -> reviewService.createReview(member.getEmail(), orderProductId, new ReviewCreateRequestDto())
        );

        verify(memberRepository).findByEmail(member.getEmail());
        verify(orderProductRepository).findById(orderProductId);
        verify(reviewRepository, never()).save(any());

    }
}
