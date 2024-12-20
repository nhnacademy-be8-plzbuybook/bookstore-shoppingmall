package com.nhnacademy.book.point.service.Impl;

import com.nhnacademy.book.book.service.Impl.CategoryService;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberPointServiceImpl implements MemberPointService {
    private final MemberPointRepository memberPointRepository;
    private final MemberRepository memberRepository;
    private final PointConditionRepository pointConditionRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Override
    public MemberPointAddResponseDto addMemberPoint(MemberPointAddRequestDto memberPointAddRequestDto) {
        Member member = memberRepository.findById(memberPointAddRequestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        PointCondition pointCondition = pointConditionRepository.findByPointConditionName(memberPointAddRequestDto.getPointConditionName())
                .orElseThrow(() -> new PointConditionNotFoundException("포인트 조건이 존재하지 않습니다."));

        BigDecimal pointsToAdd = BigDecimal.ZERO;

        // 회원가입시
        if (pointCondition.getName() == PointConditionName.SIGN_UP) {
            pointsToAdd = new BigDecimal("5000");
        }

//        // 도서구매시
//        else if (pointCondition.getName() == PointConditionName.BOOK_PURCHASE) {
//            if (memberPointAddRequestDto.getOrderStatus != 5) {
//                throw new IllegalStateException("구매확정 이후에만 포인트가 적립됩니다.");
//            }
//        }
//
//        // 결제 금액을 기분으로 포인트 계산 (payment 테이블에서 결제된 금액 가져옴)
//        Long orderId = memberPointAddRequestDto.getOrderId();
//        Payment payment = paymentRepository.findByOrderId(orderId)
//                .orElseThrow(() -> new NotFoundException("결제 정보가 존재하지 않습니다"));
//
//        BigDecimal paymentPrice = payment.getPrice();
//
//        // 기본 5%
//        pointsToAdd = paymentPrice.multiply(new BigDecimal("0.05"));
//
//
//        // 회원 등급에 따라 포인트 적립
//        String memberGrade = member.getMemberGrade();
//            switch (memberGrade) {
//                case "NORMAL":
//                    pointsToAdd = pointsToAdd.add(paymentPrice.multiply(new BigDecimal("0.01")));
//                    break;
//                case "ROYAL":
//                    pointsToAdd = pointsToAdd.add(paymentPrice.multiply(new BigDecimal("0.02")));
//                    break;
//                case "GOLD":
//                    pointsToAdd = pointsToAdd.add(paymentPrice.multiply(new BigDecimal("0.03")));
//                    break;
//                case "PLATINUM":
//                    pointsToAdd = pointsToAdd.add(paymentPrice.multiply(new BigDecimal("0.03")));
//                    break;
//                default:
//                    break;
//
//            }

        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setMember(member);
        memberPoint.setPointCondition(pointCondition);
        memberPoint.setPoint(pointsToAdd);
        memberPoint.setAddDate(LocalDateTime.now());
        memberPoint.setEndDate(LocalDateTime.now().plusYears(1)); // 유효기한 1년
        memberPointRepository.save(memberPoint);

//    }


//        // 리뷰 작성시
//        @Override
//        public void addReviewPoint(Review review) {
//            pointsToAdd = new BigDecimal("200");
//
//            if (reviewImageRepository.existsByReviewId(review.getId())) {
//                pointsToAdd = new BigDecimal("500");
//            }
//
//            Member member = review.getMember();
//            MemberPointAddRequestDto memberPointAddRequestDto = new MemberPointAddRequestDto(
//                    member.getMemberId(), PointConditionName.REVIEW.name(), pointsToAdd);
//
//            addMemberPoint(memberPointAddRequestDto);
//        }

    }
}













