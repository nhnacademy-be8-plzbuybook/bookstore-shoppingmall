package com.nhnacademy.book.member.domain.controller;


import com.nhnacademy.book.member.domain.dto.MemberModifyByAdminRequestDto;
import com.nhnacademy.book.cart.service.CartService;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CartService cartService;

    //회원 생성
    @PostMapping("/members")
    public ResponseEntity<MemberCreateResponseDto> createMember(@RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        MemberCreateResponseDto responseDto = memberService.createMember(memberCreateRequestDto);
        // 회원 가입시 장바구니 생성
        cartService.createCart(memberCreateRequestDto.getEmail());
        return ResponseEntity.ok(responseDto);
    }

    //회원 수정
    @PutMapping("/members/{member_id}")
    public ResponseEntity<MemberModifyResponseDto> modifyMember(
            @PathVariable Long member_id,
            @RequestBody MemberModifyRequestDto memberModifyRequestDto) {

        MemberModifyResponseDto responseDto = memberService.modify(member_id, memberModifyRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    //회원 수정(header를 통해)
    @PostMapping("/members/me")
    public ResponseEntity<String> updateMember(@RequestHeader("X-USER-ID") String email, @RequestBody MemberModifyRequestDto memberModifyRequestDto) {
        memberService.updateMember(email, memberModifyRequestDto);
        return ResponseEntity.ok("수정 되었습니다!");
    }


    //특정 회원 조회(이메일)
    @GetMapping("/members/email")
    public ResponseEntity<MemberEmailResponseDto> getMemberByEmail(@RequestParam String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    //특정 회원 조회(이메일 - myPage 에서 사용)
    @GetMapping("/members/my/email")
    public ResponseEntity<MemberDto> getMemberMyByEmail(@RequestHeader("X-USER-ID") String email){
        return ResponseEntity.ok(memberService.getMemberMyByEmail(email));
    }


    //특정 회원 조회(id)
    @GetMapping("/members/{member_id}")
    public ResponseEntity<MemberIdResponseDto> getMemberById(@PathVariable Long member_id) {
        return ResponseEntity.ok(memberService.getMemberById(member_id));
    }

    //회원 삭제( => withdrawal로 상태 변경)
    @PutMapping("/members/{member_id}/withdrawal")
    public ResponseEntity<String> withdrawMember(@PathVariable Long member_id) {
        memberService.withdrawMember(member_id);
        return ResponseEntity.ok("탈퇴 처리 됐습니다.");
    }

    // 회원 탈퇴
    @PostMapping("/members/withdrawal")
    public ResponseEntity<String> withdrawState(@RequestHeader("X-USER-ID") String email) {
        memberService.withdrawState(email);
        return ResponseEntity.ok("탈퇴 처리 됐습니다.");

    }

    //전체 회원 조회
    @GetMapping("/members")
    public ResponseEntity<Page<MemberSearchResponseDto>> getMembers(@ModelAttribute MemberSearchRequestDto memberSearchRequestDto) {
        return ResponseEntity.ok(memberService.getMembers(memberSearchRequestDto));
    }

    // 회원 상태 변경 (dormant → active)
    @PostMapping("/members/status/active")
    public ResponseEntity<String> updateActiveStatus(@RequestHeader("X-USER-ID") String email) {
        memberService.updateActiveStatus(email);
        return ResponseEntity.ok("회원 상태가 ACTIVE로 변경되었습니다.");
    }

    // 회원 상태 변경 (active -> dormant)
    @PostMapping("/members/status/dormant")
    public ResponseEntity<String> updateDormantStatus() {
        memberService.updateDormantStatus();
        return ResponseEntity.ok("3개월 이상 미로그인 회원이 DORMANT로 변경되었습니다.");
    }


    @PostMapping("/members/email")
    public ResponseEntity<Void> updateEmail(@RequestHeader("X-USER-ID") String email, @RequestBody MemberModifyByAdminRequestDto memberModifyByAdminRequestDto) {
        memberService.updateMemberByAdmin(email, memberModifyByAdminRequestDto);
        return ResponseEntity.ok().build();
    }
}
