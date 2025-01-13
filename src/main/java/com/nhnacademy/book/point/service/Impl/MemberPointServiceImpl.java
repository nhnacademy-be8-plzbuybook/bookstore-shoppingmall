package com.nhnacademy.book.point.service.Impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.exception.PointConditionNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import com.nhnacademy.book.point.domain.MemberPoint;
import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;
import com.nhnacademy.book.point.repository.MemberPointRepository;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import com.nhnacademy.book.review.repository.ReviewImageRepository;
import com.nhnacademy.book.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
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
    private final PaymentRepository paymentRepository;
    private final PointConditionServiceImpl pointConditionServiceImpl;

    // 회원 가입시
    @Override
    public void addSignUpPoint(Member member) {
        PointCondition pointCondition = pointConditionRepository.findByName("SIGN_UP")
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));

        int pointsToAdd = pointCondition.getConditionPoint();

        MemberPointAddRequestDto requestDto = new MemberPointAddRequestDto(
                member.getMemberId(),
                null,
                "SIGN_UP",
                pointsToAdd,
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

        int pointsToAdd = requestDto.getConditionPoint();


        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setMember(member);
        memberPoint.setPointCondition(pointCondition);
        memberPoint.setPoint(pointsToAdd);
        memberPoint.setAddDate(LocalDateTime.now());
        memberPoint.setEndDate(LocalDateTime.now().plusYears(1));
        memberPoint.setType("SAVE");
        memberPointRepository.save(memberPoint);

        return new MemberPointAddResponseDto(
                memberPoint.getMemberPointId(),
                member.getMemberId(),
                pointCondition.getName(),
                pointsToAdd,
                memberPoint.getAddDate(),
                memberPoint.getEndDate(),
                memberPoint.getType()
        );


    }

    @Override
    public List<MemberPointListResponseDto> getMemberPointsByMemberId(Long memberId) {
        List<MemberPoint> points = memberPointRepository.findAllByMember_MemberId(memberId);
        return points.stream()
                .map(point -> new MemberPointListResponseDto(
                        point.getMemberPointId(),
                        point.getMember().getMemberId(),
                        point.getPointCondition().getName(),
                        point.getPoint(),
                        point.getAddDate(),
                        point.getEndDate(),
                        point.getUsingDate(),
                        point.getType()
                ))
                .collect(Collectors.toList());
    }

    // 사용 가능 포인트 조회
    @Override
    public int getAvailablePoints(String email) {
        List<MemberPoint> points = memberPointRepository.findByMember_email(email);

        return points.stream()
                .filter(point -> point.getEndDate() != null && point.getEndDate().isAfter(LocalDateTime.now()))
                .mapToInt(MemberPoint::getPoint)
                .sum();
    }


    // 포인트 사용
    @Override
    public void usedPoint(String email, Integer usedPoint) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        int availablePoints = getAvailablePoints(email);

        if (availablePoints < usedPoint) {
            throw new IllegalArgumentException("사용 가능한 포인트가 부족합니다.");
        }

        List<MemberPoint> points = memberPointRepository.findByMember_email(email);

        for (MemberPoint point : points) {
            // 유효기간 체크
            if ((point.getEndDate() == null || point.getEndDate().isAfter(LocalDateTime.now())) && usedPoint > 0) {
                int deductable = Math.min(point.getPoint(), usedPoint);

                // 기존 포인트 차감
                point.setPoint(point.getPoint() - deductable); // 기존 포인트 업데이트
                usedPoint -= deductable; // 남은 포인트 계산
                memberPointRepository.save(point); // 기존 포인트 업데이트

                MemberPoint usedPointRecord = new MemberPoint();
                usedPointRecord.setMember(member);
                usedPointRecord.setPoint(-deductable);
                usedPointRecord.setUsingDate(LocalDateTime.now());
                usedPointRecord.setType("USE");
                usedPointRecord.setPointCondition(point.getPointCondition());

                memberPointRepository.save(usedPointRecord);
            }
        }

        if (usedPoint > 0) {
            throw new IllegalStateException("포인트 차감 중 문제가 발생했습니다. 남은 포인트: ");
        }
    }



}
