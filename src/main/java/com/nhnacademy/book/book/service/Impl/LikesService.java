package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.book.entity.Likes;
import com.nhnacademy.book.book.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikesService {
    private final MemberRepository memberRepository;
    private final SellingBookRepository sellingBookRepository;
    private final LikesRepository likesRepository;

    public Long toggleLikeBook( String email,Long sellingBookId) {
        log.info("좋아요 요청 처리 시작 - 판매책 ID: {}, 회원 이메일: {}", sellingBookId, email);

        // 1. 회원 이메일로 사용자 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 이메일이 존재하지 않습니다."));
        log.info("회원 조회 성공: {}", member.getEmail());

        // 2. 판매책 ID로 책 찾기
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
                .orElseThrow(() -> new IllegalArgumentException("판매책 ID가 유효하지 않습니다."));
        log.info("판매책 조회 성공: {}", sellingBook.getSellingBookId());

        // 3. 좋아요 존재 여부 확인
        Optional<Likes> existingLike = likesRepository.findByMemberAndSellingBook(member, sellingBook);
        if (existingLike.isPresent()) {
            // 좋아요가 이미 존재하면 삭제
            likesRepository.delete(existingLike.get());
            log.info("좋아요 취소: 회원 {}, 판매책 {}", member.getEmail(), sellingBook.getSellingBookId());
        } else {
            // 좋아요가 없으면 추가
            Likes like = new Likes();
            like.setMember(member);
            like.setSellingBook(sellingBook);
            likesRepository.save(like);
            log.info("좋아요 추가: 회원 {}, 판매책 {}", member.getEmail(), sellingBook.getSellingBookId());
        }

        // 4. 현재 좋아요 수 반환
        Long likeCount = likesRepository.countBySellingBook(sellingBook);
        log.info("현재 좋아요 수: {}", likeCount);
        return likeCount;
    }
}
