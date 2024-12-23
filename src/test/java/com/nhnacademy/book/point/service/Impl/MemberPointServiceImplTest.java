package com.nhnacademy.book.point.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.Review;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.ReviewImageRepository;
import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.domain.PointConditionName;
import com.nhnacademy.book.point.repository.MemberPointRepository;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberPointServiceImplTest {

    @Mock
    private MemberPointRepository memberPointRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PointConditionRepository pointConditionRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @InjectMocks
    private MemberPointServiceImpl memberPointService;

    private Member member;


    @BeforeEach
    void setup() {
        member = new Member();
        member.setMemberId(1L);
    }

    @Test
    @DisplayName("회원가입 시 포인트 적립 성공")
    void addSignUpPoint() {
        PointCondition pointCondition = new PointCondition();
        pointCondition.setName(PointConditionName.SIGN_UP);
        pointCondition.setConditionPoint(5000);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(pointConditionRepository.findByName(PointConditionName.SIGN_UP))
                .thenReturn(Optional.of(pointCondition));

        memberPointService.addSignUpPoint(member);
        verify(memberPointRepository, times(1)).save(any());

    }


//    @Test
//    @DisplayName("리뷰 작성 시 포인트 적립 성공 (이미지 포함)")
//    void addReviewPointWithImage() {
//        Review review = new Review();
//        review.setReviewId(1L);
//        review.setMember(member);
//
//        PointCondition pointCondition = new PointCondition();
//        pointCondition.setName(PointConditionName.REVIEW);
//        pointCondition.setConditionPoint(200);
//
//        PointCondition photoReviewCondition = new PointCondition();
//        photoReviewCondition.setName(PointConditionName.PHOTO_REVIEW);
//        photoReviewCondition.setConditionPoint(300); // 사진 리뷰 300 추가
//
//        // 리뷰에 이미지가 포함된 경우
//        when(reviewImageRepository.existsByReview_ReviewId(1L)).thenReturn(true);
//        when(pointConditionRepository.findByName(PointConditionName.REVIEW))
//                .thenReturn(Optional.of(pointCondition));
//        when(pointConditionRepository.findByName(PointConditionName.PHOTO_REVIEW))
//                .thenReturn(Optional.of(photoReviewCondition));
//
//        memberPointService.addReviewPoint(review);
//        verify(memberPointRepository, times(1)).save(any());
//    }


}
