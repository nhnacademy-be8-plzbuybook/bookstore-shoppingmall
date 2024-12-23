package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.entity.Likes;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.LikesRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final SellingBookRepository sellingBookRepository;

    /**
     * 회원이 책에 좋아요를 추가
     *
     * @param memberId 회원 ID
     * @param sellingBookId 판매책 ID
     */
    public void likeBook(Long memberId, Long sellingBookId) {
        // 회원이 존재하는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 판매책이 존재하는지 확인
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
                .orElseThrow(() -> new IllegalArgumentException("판매책이 존재하지 않습니다."));

        // 이미 좋아요한 경우 처리
        if (likesRepository.existsByMemberAndSellingBook(member, sellingBook)) {
            throw new IllegalStateException("이미 이 책을 좋아요하였습니다.");
        }

        // 좋아요 데이터 저장
        Likes likes = new Likes();
        likes.setMember(member);
        likes.setSellingBook(sellingBook);

        likesRepository.save(likes);
    }
}

