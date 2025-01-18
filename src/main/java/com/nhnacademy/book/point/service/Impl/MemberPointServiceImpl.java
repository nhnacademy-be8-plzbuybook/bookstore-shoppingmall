package com.nhnacademy.book.point.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberGradeNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.exception.PointConditionNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.point.domain.MemberPoint;
import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.dto.MemberPointAddRequestDto;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;
import com.nhnacademy.book.point.repository.MemberPointRepository;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import com.nhnacademy.book.review.domain.Review;
import com.nhnacademy.book.review.repository.ReviewImageRepository;
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
    private final ReviewImageRepository reviewImageRepository;
    private final OrderProductRepository orderProductRepository;

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

    // 도서구매시
    public void addPurchasePoint(Member member, OrderRequestDto orderRequest) {
        PointCondition pointCondition = pointConditionRepository.findByName("BOOK_PURCHASE")
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));
        BigDecimal basePercentage = pointCondition.getConditionPercentage();

        BigDecimal gradePercentage = switch (member.getMemberGrade().getMemberGradeName()) {
            case "NORMAL" -> new BigDecimal("0.01");
            case "ROYAL" -> new BigDecimal("0.02");
            case "GOLD" -> new BigDecimal("0.03");
            case "PLATINUM" -> new BigDecimal("0.04");
            default -> throw new MemberGradeNotFoundException("회원 등급이 존재하지 않습니다.");
        };
        BigDecimal totalPercentage = basePercentage.add(gradePercentage);

        BigDecimal accumulatedAmount = orderProductRepository.findLatestOrderTotalPriceByMemberId(member.getMemberId());

        BigDecimal totalPoints = accumulatedAmount.multiply(totalPercentage);

        // 포인트 적립 기록 추가
        MemberPointAddRequestDto requestDto = new MemberPointAddRequestDto(
                member.getMemberId(),
                null,
                "BOOK_PURCHASE",
                totalPoints.intValue(),
                totalPercentage
        );
        addMemberPoint(requestDto);
    }


    @Override
    public void addReviewPoint(Review review) {
        PointCondition reviewCondition = pointConditionRepository.findByName("REVIEW")
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));
        PointCondition photoReviewCondition = pointConditionRepository.findByName("PHOTO_REVIEW")
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));

        String pointConditionName;
        int pointsToAdd;

        // 사진 여부에 따라 포인트 조건 설정
        if (reviewImageRepository.existsByReview_ReviewId(review.getReviewId())) {
            pointConditionName = "PHOTO_REVIEW";
            pointsToAdd = photoReviewCondition.getConditionPoint();
        } else {
            pointConditionName = "REVIEW";
            pointsToAdd = reviewCondition.getConditionPoint();
        }

        // 포인트 기록 추가
        addMemberPoint(new MemberPointAddRequestDto(
                review.getMember().getMemberId(),
                review.getReviewId(),
                pointConditionName,
                pointsToAdd,
                null
        ));
    }

    @Override
    public void updatePointForReview(Review review, boolean isPhotoAdded) {
        PointCondition photoReview = pointConditionRepository.findByName("PHOTO_ADD")
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));
        PointCondition photoRemove = pointConditionRepository.findByName("PHOTO_REMOVE")
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));

        if (isPhotoAdded) {
            // 사진 추가 시 포인트 지급
            if (!review.isPhotoPointGiven()) {
                addMemberPoint(new MemberPointAddRequestDto(
                        review.getMember().getMemberId(),
                        review.getReviewId(),
                        photoReview.getName(),
                        photoReview.getConditionPoint(),
                        null
                ));
                //사진 추가 포인트 지급함
                review.setPhotoPointGiven(true);
            }
        } else {
            // 사진 삭제 시 포인트 차감
            if (review.isPhotoPointGiven()) {
                addMemberPoint(new MemberPointAddRequestDto(
                        review.getMember().getMemberId(),
                        review.getReviewId(),
                        photoRemove.getName(),
                        -photoRemove.getConditionPoint(),
                        null
                ));
                review.setPhotoPointGiven(false); // 포인트 지급 상태 업데이트
            }
        }
    }

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
        memberPoint.setType(requestDto.getConditionPoint() < 0 ? "USE" : "SAVE");
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
        List<MemberPoint> points = memberPointRepository.findAllByMemberIdOrderByDateDesc(memberId);

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

        int totalSavedPoints = points.stream()
                .filter(point -> point.getEndDate() != null && point.getEndDate().isAfter(LocalDateTime.now()))
                .filter(point -> "SAVE".equals(point.getType()))
                .mapToInt(MemberPoint::getPoint)
                .sum();

        int totalUsedPoints = points.stream()
                .filter(point -> "USE".equals(point.getType()))
                .mapToInt(point -> Math.abs(point.getPoint()))
                .sum();

        // 사용 가능 포인트 = 적립된 포인트 - 사용된 포인트
        return totalSavedPoints - totalUsedPoints;
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

        if (availablePoints - usedPoint < 0) {
            throw new IllegalArgumentException("사용 후 포인트 총합이 음수가 될 수 없습니다.");
        }

        List<MemberPoint> points = memberPointRepository.findByMember_email(email);

        for (MemberPoint point : points) {
            // 유효기간 체크
            if ((point.getEndDate() == null || point.getEndDate().isAfter(LocalDateTime.now())) && usedPoint > 0) {
                int deductable = Math.min(point.getPoint(), usedPoint);

                usedPoint -= deductable; // 남은 포인트 계산

                // 포인트 사용 기록 추가
                MemberPoint usedPointRecord = new MemberPoint();
                usedPointRecord.setMember(member);
                usedPointRecord.setPoint(-deductable); // 사용된 포인트
                usedPointRecord.setUsingDate(LocalDateTime.now());
                usedPointRecord.setType("USE");

                PointCondition condition = pointConditionRepository.findById(5L)
                        .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));
                usedPointRecord.setPointCondition(condition);

                memberPointRepository.save(usedPointRecord);

                if (usedPoint == 0) {
                    break;
                }
            }

        }

    }


    // 반품시 포인트 지급
    @Override
    public void restorePoint(Member member, int points) {
        PointCondition pointCondition = pointConditionRepository.findByName("RETURN").orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));

        MemberPoint returnPoint = new MemberPoint();
        returnPoint.setMember(member);
        returnPoint.setPoint(points);
        returnPoint.setPointCondition(pointCondition);
        returnPoint.setAddDate(LocalDateTime.now());
        returnPoint.setEndDate(LocalDateTime.now().plusYears(1));
        returnPoint.setType("SAVE");

        memberPointRepository.save(returnPoint);

    }

}
