package com.nhnacademy.book.point.service.Impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.Review;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.exception.PointConditionNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.ReviewImageRepository;
import com.nhnacademy.book.member.domain.repository.ReviewRepository;
import com.nhnacademy.book.point.domain.MemberPoint;
import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.domain.PointConditionName;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.point.repository.MemberPointRepository;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Transactional
@Service
@RequiredArgsConstructor
public class MemberPointServiceImpl implements MemberPointService {
    private final MemberPointRepository memberPointRepository;
    private final MemberRepository memberRepository;
    private final PointConditionRepository pointConditionRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final MemberGradeRepository memberGradeRepository;
//    private final PaymentRepository paymentRepository;

    // 회원 가입시
    @Override
    public void addSignUpPoint(Member member) {
        PointCondition pointCondition = pointConditionRepository.findByName(PointConditionName.SIGN_UP)
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));

        BigDecimal pointsToAdd = new BigDecimal(pointCondition.getConditionPoint());

        MemberPointAddRequestDto requestDto = new MemberPointAddRequestDto(
                member.getMemberId(),
                null,
                PointConditionName.SIGN_UP,
                pointsToAdd.intValue(),
                null
        );

        addMemberPoint(requestDto);
    }

//    // 도서 구매시
//    @Override
//    public void addBookPurchasePoint(Member member, Long orderId, Integer orderStatus) {
//        if (orderStatus != 5) { // 5는 구매확정 상태
//            throw new IllegalStateException("구매확정 이후에만 포인트가 적립됩니다.");
//        }
//
//        // 결제 정보를 Payment 테이블에서 가져오는 부분 (결제 금액을 기준으로 포인트 계산)
//        Payment payment = paymentRepository.findByOrderId(orderId)
//                .orElseThrow(() -> new NotFoundException("결제 정보가 존재하지 않습니다"));
//
//        // 결제 금액을 가져옴 (실제 결제 금액을 기준으로 포인트 계산)
//        BigDecimal paymentPrice = payment.getPrice();
//
//        PointCondition pointCondition = pointConditionRepository.findByName(PointConditionName.BOOK_PURCHASE)
//                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));
//
//        // 기본 포인트 5%
//        BigDecimal conditionPercentage = pointCondition.getConditionPercentage();
//        BigDecimal pointsToAdd = paymentPrice.multiply(conditionPercentage);
//
//        BigDecimal gradePercentage = BigDecimal.ZERO;
//
//        switch (member.getMemberGrade().getMemberGradeName()) {
//            case "NORMAL":
//                gradePercentage = new BigDecimal("0.01");  // 일반 등급 1%
//                break;
//            case "ROYAL":
//                gradePercentage = new BigDecimal("0.02");  // 로얄 등급 2%
//                break;
//            case "GOLD":
//                gradePercentage = new BigDecimal("0.03");  // 골드 등급 3%
//                break;
//            case "PLATINUM":
//                gradePercentage = new BigDecimal("0.03");  // 플래티넘 등급 3%
//                break;
//            default:
//                throw new IllegalStateException("알 수 없는 등급입니다.");
//        }
//
//
//        // 등급에 따른 포인트 추가
//        pointsToAdd = pointsToAdd.add(paymentPrice.multiply(gradePercentage));
//
//        // 포인트 적립을 위한 MemberPointAddRequestDto 생성
//        MemberPointAddRequestDto requestDto = new MemberPointAddRequestDto(
//                member.getMemberId(),
//                null,
//                PointConditionName.BOOK_PURCHASE,
//                pointsToAdd.intValue(),
//                conditionPercentage
//        );
//        addMemberPoint(requestDto);
//
//    }
//
//
//    // 리뷰 작성시
//    @Override
//    public void addReviewPoint(Review review) {
//        PointCondition pointCondition = pointConditionRepository.findByName(PointConditionName.REVIEW)
//                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));
//
//        BigDecimal pointsToAdd = new BigDecimal(pointCondition.getConditionPoint()); // 기본 200
//
//        // 이미지 여부 확인
//        if (reviewImageRepository.existsByReview_ReviewId(review.getReviewId())) {
//            PointCondition photoReviewCondition = pointConditionRepository.findByName(PointConditionName.PHOTO_REVIEW)
//                    .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));
//
//            pointsToAdd = pointsToAdd.add(new BigDecimal(photoReviewCondition.getConditionPoint())); // 추가 300
//
//        }
//
//        MemberPointAddRequestDto requestDto = new MemberPointAddRequestDto(
//                review.getMember().getMemberId(),
//                review.getReviewId(),
//                PointConditionName.REVIEW,
//                pointsToAdd.intValue(),
//                null
//        );
//        addMemberPoint(requestDto);
//
//    }
//



    @Override
    public MemberPointAddResponseDto addMemberPoint(MemberPointAddRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        PointCondition pointCondition = pointConditionRepository.findByName(requestDto.getName())
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));

        BigDecimal pointsToAdd = new BigDecimal(requestDto.getConditionPoint());


        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setMember(member);
        memberPoint.setPointCondition(pointCondition);
        memberPoint.setPoint(pointsToAdd);
        memberPoint.setAddDate(LocalDateTime.now());
        memberPoint.setEndDate(LocalDateTime.now().plusYears(1)); // 유효기한 1년
        memberPointRepository.save(memberPoint);

        return new MemberPointAddResponseDto(
                memberPoint.getMemberPointId(),
                member.getMemberId(),
                pointCondition.getName().toString(),
                pointsToAdd,
                memberPoint.getAddDate(),
                memberPoint.getEndDate()
        );


    }

}














