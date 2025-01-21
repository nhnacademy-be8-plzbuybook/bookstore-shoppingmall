package com.nhnacademy.book.point.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.point.domain.MemberPoint;
import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;
import com.nhnacademy.book.point.repository.MemberPointRepository;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import com.nhnacademy.book.review.domain.Review;
import com.nhnacademy.book.review.repository.ReviewImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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

    @Mock
    private OrderProductRepository orderProductRepository;

    @InjectMocks
    private MemberPointServiceImpl memberPointService;


    @Test
    @DisplayName("회원가입 시 포인트 적립 성공")
    void addSignUpPoint_Success() {

        Member member = new Member();
        member.setMemberId(1L);

        PointCondition pointCondition = new PointCondition();
        pointCondition.setName("SIGN_UP");
        pointCondition.setConditionPoint(5000);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(pointConditionRepository.findByName("SIGN_UP"))
                .thenReturn(Optional.of(pointCondition));

        memberPointService.addSignUpPoint(member);
        verify(memberPointRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("도서 구매시 포인트 적립 성공")
    void addPurchasePoint_Success() {
        Long memberId = 1L;
        BigDecimal accumulatedAmount = new BigDecimal("21000");

        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName("GOLD");

        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");

        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberGrade(memberGrade);
        member.setMemberStatus(memberStatus);

        PointCondition pointCondition = new PointCondition();
        pointCondition.setName("BOOK_PURCHASE");
        pointCondition.setConditionPercentage(new BigDecimal("0.05"));

        when(pointConditionRepository.findByName("BOOK_PURCHASE")).thenReturn(Optional.of(pointCondition));
        when(orderProductRepository.findLatestOrderTotalPriceByMemberId(memberId)).thenReturn(accumulatedAmount);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        memberPointService.addPurchasePoint(member, mock(OrderRequestDto.class));

        BigDecimal basePercentage = new BigDecimal("0.05"); // 기본 비율
        BigDecimal gradePercentage = new BigDecimal("0.03"); // GOLD 등급 추가 비율
        BigDecimal totalPercentage = basePercentage.add(gradePercentage); // 총 비율: 0.08
        BigDecimal expectedPoints = accumulatedAmount.multiply(totalPercentage); // 21,000 * 0.08 = 1,680

        verify(memberPointRepository, times(1)).save(argThat(memberPoint ->
                memberPoint.getMember().getMemberId().equals(memberId) &&
                        memberPoint.getPoint() == expectedPoints.intValue() &&
                        memberPoint.getPointCondition().getName().equals("BOOK_PURCHASE")
        ));


    }


    @Test
    @DisplayName("사진리뷰작성시 포인트 적립 성공")
    void addReviewPoint_WithPhoto_Success() {
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(1L);

        Review review = new Review();
        review.setReviewId(100L);
        review.setMember(member);

        PointCondition photoReviewCondition = new PointCondition();
        photoReviewCondition.setName("PHOTO_REVIEW");
        photoReviewCondition.setConditionPoint(500);

        PointCondition reviewCondition = new PointCondition();
        reviewCondition.setName("REVIEW");
        reviewCondition.setConditionPoint(200);

        when(pointConditionRepository.findByName("PHOTO_REVIEW")).thenReturn(Optional.of(photoReviewCondition));
        when(pointConditionRepository.findByName("REVIEW")).thenReturn(Optional.of(reviewCondition));
        when(reviewImageRepository.existsByReview_ReviewId(100L)).thenReturn(true); // 사진 있음
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        memberPointService.addReviewPoint(review);

        verify(memberPointRepository).save(argThat(memberPoint ->
                memberPoint.getMember().getMemberId().equals(1L) &&
                        memberPoint.getPointCondition().getName().equals("PHOTO_REVIEW") &&
                        memberPoint.getPoint() == 500 &&
                        memberPoint.getType().equals("SAVE")
        ));


    }
    @Test
    @DisplayName("리뷰작성시 포인트 적립 성공")
    void addReviewPoint__Success() {
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(1L);

        Review review = new Review();
        review.setReviewId(100L);
        review.setMember(member);

        PointCondition photoReviewCondition = new PointCondition();
        photoReviewCondition.setName("PHOTO_REVIEW");
        photoReviewCondition.setConditionPoint(500);

        PointCondition reviewCondition = new PointCondition();
        reviewCondition.setName("REVIEW");
        reviewCondition.setConditionPoint(200);

        when(pointConditionRepository.findByName("PHOTO_REVIEW")).thenReturn(Optional.of(photoReviewCondition));
        when(pointConditionRepository.findByName("REVIEW")).thenReturn(Optional.of(reviewCondition));
        when(reviewImageRepository.existsByReview_ReviewId(100L)).thenReturn(false);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        memberPointService.addReviewPoint(review);

        verify(memberPointRepository).save(argThat(memberPoint ->
                memberPoint.getMember().getMemberId().equals(1L) &&
                        memberPoint.getPointCondition().getName().equals("REVIEW") &&
                        memberPoint.getPoint() == 200 &&
                        memberPoint.getType().equals("SAVE")
        ));


    }

    @Test
    @DisplayName("리뷰 사진 추가 시 포인트 적립 성공")
    void updatePointForReview_AddPhoto_Success() {
        Long memberId = 1L;
        Long reviewId = 100L;

        Member member = new Member();
        member.setMemberId(memberId);

        Review review = new Review();
        review.setReviewId(reviewId);
        review.setMember(member);
        review.setPhotoPointGiven(false);

        PointCondition photoReview = new PointCondition();
        photoReview.setName("PHOTO_ADD");
        photoReview.setConditionPoint(300);

        PointCondition photoRemove = new PointCondition();
        photoRemove.setName("PHOTO_REMOVE");
        photoRemove.setConditionPoint(300);

        when(pointConditionRepository.findByName("PHOTO_ADD")).thenReturn(Optional.of(photoReview));
        when(pointConditionRepository.findByName("PHOTO_REMOVE")).thenReturn(Optional.of(photoRemove));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        memberPointService.updatePointForReview(review, true);

        verify(memberPointRepository).save(argThat(memberPoint ->
                memberPoint.getMember().getMemberId().equals(memberId) &&
                        memberPoint.getPointCondition().getName().equals("PHOTO_ADD") &&
                        memberPoint.getPoint() == 300
        ));
        assertTrue(review.isPhotoPointGiven());

    }


    @Test
    @DisplayName("리뷰 사진 삭제 시 포인트 차감 성공")
    void updatePointForReview_RemovePhoto_Success() {
        Long memberId = 1L;
        Long reviewId = 100L;

        Member member = new Member();
        member.setMemberId(memberId);

        Review review = new Review();
        review.setReviewId(reviewId);
        review.setMember(member);
        review.setPhotoPointGiven(true);

        PointCondition photoReview = new PointCondition();
        photoReview.setName("PHOTO_ADD");
        photoReview.setConditionPoint(300);

        PointCondition photoRemove = new PointCondition();
        photoRemove.setName("PHOTO_REMOVE");
        photoRemove.setConditionPoint(300);

        when(pointConditionRepository.findByName("PHOTO_ADD")).thenReturn(Optional.of(photoReview));
        when(pointConditionRepository.findByName("PHOTO_REMOVE")).thenReturn(Optional.of(photoRemove));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        memberPointService.updatePointForReview(review, false);

        verify(memberPointRepository).save(argThat(memberPoint ->
                memberPoint.getMember().getMemberId().equals(memberId) &&
                        memberPoint.getPointCondition().getName().equals("PHOTO_REMOVE") &&
                        memberPoint.getPoint() == -300
        ));
        assertFalse(review.isPhotoPointGiven());

    }

    @Test
    @DisplayName("회원 포인트 목록 조회 성공")
    void getMemberPointsByMemberId_Success() {
        Long memberId = 1L;

        Member member = new Member();
        member.setMemberId(memberId);

        PointCondition pointCondition = new PointCondition();
        pointCondition.setName("BOOK_PURCHASE");

        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setMember(member);
        memberPoint.setPointCondition(pointCondition);
        memberPoint.setPoint(500);
        memberPoint.setAddDate(LocalDateTime.now());
        memberPoint.setEndDate(LocalDateTime.now().plusYears(1));
        memberPoint.setType("SAVE");

        when(memberPointRepository.findAllByMemberIdOrderByDateDesc(memberId))
                .thenReturn(List.of(memberPoint));

        List<MemberPointListResponseDto> result = memberPointService.getMemberPointsByMemberId(memberId);

    }


    @Test
    @DisplayName("사용 가능 포인트 조회 성공")
    void getAvailablePoints_Success() {
        String email = "test@naver.com";

        MemberPoint savedPoint1 = new MemberPoint();
        savedPoint1.setPoint(2000);
        savedPoint1.setAddDate(LocalDateTime.now());
        savedPoint1.setEndDate(LocalDateTime.now().plusYears(1));
        savedPoint1.setType("SAVE");

        MemberPoint savedPoint2 = new MemberPoint();
        savedPoint2.setPoint(3000);
        savedPoint2.setAddDate(LocalDateTime.now());
        savedPoint2.setEndDate(LocalDateTime.now().minusDays(1));
        savedPoint1.setType("SAVE");

        MemberPoint usedPoint = new MemberPoint();
        usedPoint.setPoint(-200);
        usedPoint.setUsingDate(LocalDateTime.now());
        usedPoint.setType("USE");

        when(memberPointRepository.findByMember_email(email))
                .thenReturn(List.of(savedPoint1, savedPoint2, usedPoint));

        int availablePoints = memberPointService.getAvailablePoints(email);

        assertEquals(1800, availablePoints);
        verify(memberPointRepository, times(1)).findByMember_email(email);

    }

    @Test
    @DisplayName("모든 적립 포인트가 만료된 경우")
    void getAvailablePoints_AllPointsExpired() {
        String email = "test@naver.com";

        // 만료된 포인트
        MemberPoint expiredPoint1 = new MemberPoint();
        expiredPoint1.setPoint(1000);
        expiredPoint1.setEndDate(LocalDateTime.now().minusDays(1));
        expiredPoint1.setType("SAVE");

        MemberPoint expiredPoint2 = new MemberPoint();
        expiredPoint2.setPoint(2000);
        expiredPoint2.setEndDate(LocalDateTime.now().minusDays(1));
        expiredPoint2.setType("SAVE");

        when(memberPointRepository.findByMember_email(email))
                .thenReturn(List.of(expiredPoint1, expiredPoint2));

        int availablePoints = memberPointService.getAvailablePoints(email);

        assertEquals(0, availablePoints);
        verify(memberPointRepository, times(1)).findByMember_email(email);
    }

    @Test
    @DisplayName("모든 포인트가 사용된 경우")
    void getAvailablePoints_AllPointsUsed() {
        String email = "test@naver.com";

        MemberPoint savedPoint = new MemberPoint();
        savedPoint.setPoint(1000);
        savedPoint.setEndDate(LocalDateTime.now().plusYears(1));
        savedPoint.setType("SAVE");

        MemberPoint usedPoint = new MemberPoint();
        usedPoint.setPoint(-1500);
        usedPoint.setUsingDate(LocalDateTime.now());
        usedPoint.setType("USE");

        when(memberPointRepository.findByMember_email(email))
                .thenReturn(List.of(savedPoint, usedPoint));

        int availablePoints = memberPointService.getAvailablePoints(email);

        assertEquals(0, availablePoints);
        verify(memberPointRepository, times(1)).findByMember_email(email);
    }

    @Test
    @DisplayName("정상적인 포인트 사용")
    void usedPoint_Success() {


        String email = "test@naver.com";
        int usedPoint = 500;

        Member member = new Member();
        member.setMemberId(1L);

        PointCondition pointCondition = new PointCondition();
        pointCondition.setName("USE");

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(memberPointRepository.findByMember_email(email)).thenReturn(List.of(
                addMemberPoint(1000, "SAVE")
        ));
        when(pointConditionRepository.findByName("USE")).thenReturn(Optional.of(pointCondition));

        memberPointService.usedPoint(email, usedPoint);

        verify(memberPointRepository).save(argThat(point ->
                point.getMember().equals(member) &&
                        point.getPoint() == -usedPoint &&
                        point.getType().equals("USE") &&
                        point.getPointCondition().getName().equals("USE")
        ));
    }

    private MemberPoint addMemberPoint(int points, String type) {
        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setPoint(points);
        memberPoint.setAddDate(LocalDateTime.now());
        memberPoint.setEndDate(LocalDateTime.now().plusYears(1));
        memberPoint.setType(type);
        return memberPoint;
    }


    @Test
    @DisplayName("포인트 부족")
    void usedPoint_InsufficientPoints_ThrowException() {
        String email = "test@naver.com";
        int usedPoint = 1500;

        MemberPoint savedPoint1 = new MemberPoint();
        savedPoint1.setPoint(1000);
        savedPoint1.setType("SAVE");
        savedPoint1.setEndDate(LocalDateTime.now().plusYears(1));

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(new Member()));
        when(memberPointRepository.findByMember_email(email)).thenReturn(List.of(savedPoint1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberPointService.usedPoint(email, usedPoint));

        assertEquals("사용 가능한 포인트가 부족합니다.", exception.getMessage());
        verify(memberPointRepository, times(1)).findByMember_email(email);
    }

    @Test
    @DisplayName("반품 시 포인트 지급 성공")
    void restorePoint_Success() {
        Long memberId = 1L;
        int restorePoints = 500;

        Member member = new Member();
        member.setMemberId(memberId);

        PointCondition returnCondition = new PointCondition();
        returnCondition.setName("RETURN");

        when(pointConditionRepository.findByName("RETURN")).thenReturn(Optional.of(returnCondition));

        memberPointService.restorePoint(member, restorePoints);

        verify(memberPointRepository).save(argThat(point ->
                point.getMember().equals(member) &&
                        point.getPoint() == restorePoints &&
                        point.getPointCondition().getName().equals("RETURN") &&
                        point.getType().equals("SAVE")
        ));

        verify(pointConditionRepository, times(1)).findByName("RETURN");
    }

}
