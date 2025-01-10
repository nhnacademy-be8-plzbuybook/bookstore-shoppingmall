package com.nhnacademy.book.point.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.point.dto.MemberPointAddResponseDto;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;
import com.nhnacademy.book.point.dto.MemberPointUseRequestDto;
import com.nhnacademy.book.point.service.MemberPointService;
import com.nhnacademy.book.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberPointController {

    private final MemberPointService memberPointService;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @PostMapping("members/{member_id}/points/signup")
    public ResponseEntity<String> addSignUpPoint(@PathVariable Long member_id) {
        try {
            Member member = memberRepository.findById(member_id)
                    .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));
            memberPointService.addSignUpPoint(member);
            return new ResponseEntity<>("회원 가입 포인트가 적립되었습니다.", HttpStatus.CREATED);
        } catch (MemberNotFoundException e) {
            return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/members/{member_id}/points/purchase/{order_id}")
//    public ResponseEntity<String> addBookPurchasePoint(@PathVariable("member_id") Long member_id,
//                                                       @PathVariable("order_id") Long order_id,
//                                                       @RequestParam("orderStatus") Integer orderStatus) {
//        try {
//             Member member = memberRepository.findById(member_id)
//                    .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));
//            memberPointService.addBookPurchasePoint(member, order_id, orderStatus);
//            return new ResponseEntity<>("도서 구매 포인트가 적립되었습니다.", HttpStatus.CREATED);
//        } catch (IllegalStateException e) {
//            return new ResponseEntity<>("구매확정 이후에만 포인트가 적립됩니다.", HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>("포인트 적립 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//
//    public ResponseEntity<String> addReviewPoint(@PathVariable Long member_id, @PathVariable Long review_id) {
//        try {
//            Review review = reviewRepository.findById(review_id)
//                    .orElseThrow(() -> new ReviewNotFoundException("리뷰가 존재하지 않습니다."));
//            memberPointService.addReviewPoint(review);
//            return new ResponseEntity<>("리뷰 포인트가 적립되었습니다.", HttpStatus.CREATED);
//        } catch (ReviewNotFoundException e) {
//            return new ResponseEntity<>("리뷰가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }  catch (Exception e) {
//            return new ResponseEntity<>("포인트 적립 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//

    @GetMapping("/members/{member_id}/points")
    public ResponseEntity<List<MemberPointListResponseDto>> getMemberPoints(@PathVariable Long member_id) {
        try {
            Member member = memberRepository.findById(member_id)
                    .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));
            List<MemberPointListResponseDto> points = memberPointService.getMemberPointsByMemberId(member_id);
            return ResponseEntity.ok(points);
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
    }

    // 사용 가능한 포인트 조회 (주문서)
    @PostMapping("/points/members/available")
    public ResponseEntity<Integer> getAvailablePoints(@RequestHeader("X-USER-ID") String email) {
        int availablePoints = memberPointService.getAvailablePoints(email);
        return ResponseEntity.ok(availablePoints);
    }

    // 포인트 사용
    @PostMapping("/points/members/use")
    public ResponseEntity<String> usedPoint(@RequestBody MemberPointUseRequestDto requestDto) {
        memberPointService.usedPoint(requestDto.getEmail(), requestDto.getUsedPoint());
        return ResponseEntity.ok("포인트 사용이 완료되었습니다.");
    }


}
