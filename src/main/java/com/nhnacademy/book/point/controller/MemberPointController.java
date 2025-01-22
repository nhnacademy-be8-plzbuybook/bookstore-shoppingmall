package com.nhnacademy.book.point.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.point.dto.MemberPointListResponseDto;
import com.nhnacademy.book.point.dto.MemberPointUseRequestDto;
import com.nhnacademy.book.point.service.MemberPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberPointController {

    private final MemberPointService memberPointService;
    private final MemberRepository memberRepository;

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
