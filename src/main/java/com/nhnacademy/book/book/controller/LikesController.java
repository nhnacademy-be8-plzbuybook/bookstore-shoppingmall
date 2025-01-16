package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.service.Impl.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


//ID(이메일) : 이메일은 gateway에서 검증해준 x-user-Id를 통해 가져온다

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/member-selling-books/like/{sellingBookId}")
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


    //회원 Id를 통해 좋아요 누른 책의 list를 가져오는 api
    @GetMapping("/members/{memberId}/liked-books")
    public Page<SellingBookAndBookResponseDto> getLikedBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @PathVariable Long memberId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return likesService.getLikeBooks(memberId, pageable);

    }

}
