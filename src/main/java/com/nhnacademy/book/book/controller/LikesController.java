package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.service.Impl.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    /**
     * 회원이 책에 좋아요 추가
     *
     * @param memberId 회원 ID
     * @param sellingBookId 판매책 ID
     * @return 응답 상태
     */
    @PostMapping
    public ResponseEntity<Void> likeBook(@RequestParam Long memberId, @RequestParam Long sellingBookId) {
        likesService.likeBook(memberId, sellingBookId);
        return ResponseEntity.ok().build();
    }
}
