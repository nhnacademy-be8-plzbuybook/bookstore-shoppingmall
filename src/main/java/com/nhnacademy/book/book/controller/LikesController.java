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
import org.springframework.web.server.ResponseStatusException;


//ID(이메일) : 이메일은 gateway에서 검증해준 x-user-Id를 통해 가져온다

@RestController
@RequestMapping("/api/member-selling-books")
@RequiredArgsConstructor
@Slf4j
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/like/{sellingBookId}")
    public ResponseEntity<Long> toggleLike(
            @RequestHeader(value = "X-USER-ID") String email,
            @PathVariable Long sellingBookId

    ) {
        if (email == null) {
            log.error("X-USER-ID 헤더가 전달되지 않았습니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "X-USER-ID 헤더가 없습니다.");
        }

        log.info("좋아요 요청 - 판매책 ID: {}, 사용자 이메일: {}", sellingBookId, email);

        // 좋아요 토글 처리
        Long likeCount = likesService.toggleLikeBook( email ,sellingBookId );
        return ResponseEntity.ok(likeCount);
    }

}
