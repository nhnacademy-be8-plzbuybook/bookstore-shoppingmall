package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.service.Impl.LikesService;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//ID(이메일) : 이메일은 gateway에서 검증해준 x-user-Id를 통해 가져온다

@RestController
@RequestMapping("/api/selling-books")
@RequiredArgsConstructor
@Slf4j
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/like/{sellingBookId}")
    public ResponseEntity<Long> toggleLike(
            @PathVariable Long sellingBookId,
            @RequestHeader("X-USER-ID") String memberEmail) { // 이메일을 헤더로 받음
        log.info("좋아요 요청 - 판매책 ID: {}, 회원 이메일: {}", sellingBookId, memberEmail);

        // 좋아요 토글 처리
        Long likeCount = likesService.toggleLikeBook(sellingBookId, memberEmail);
        return ResponseEntity.ok(likeCount);
    }
}
