package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikesService {
    private final MemberRepository memberRepository;
    private final SellingBookRepository sellingBookRepository;
    private final LikesRepository likesRepository;
    private final BookImageRepository bookImageRepository;
    private final CategoryRepository categoryRepository;
    private final BookAuthorRepository bookAuthorRepository;

    public Long toggleLikeBook( String email,Long sellingBookId) {

        // 1. 회원 이메일로 사용자 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 이메일이 존재하지 않습니다."));

        // 2. 판매책 ID로 책 찾기
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
                .orElseThrow(() -> new IllegalArgumentException("판매책 ID가 유효하지 않습니다."));

        // 3. 좋아요 존재 여부 확인
        Optional<Likes> existingLike = likesRepository.findByMemberAndSellingBook(member, sellingBook);
        if (existingLike.isPresent()) {
            // 좋아요가 이미 존재하면 삭제
            likesRepository.delete(existingLike.get());
        } else {
            // 좋아요가 없으면 추가
            Likes like = new Likes();
            like.setMember(member);
            like.setSellingBook(sellingBook);
            likesRepository.save(like);
        }

        // 4. 현재 좋아요 수 반환
        return likesRepository.countBySellingBook(sellingBook);
    }

    //회원 Id를 통해 좋아요 누른 책의 list를 가져오는 api
    public Page<SellingBookAndBookResponseDto> getLikeBooks(Long memberId, Pageable pageable) {
        Page<SellingBook> likedBooks = likesRepository.findLikedBooksByMemberId(memberId, pageable);

        return likedBooks.map(this::toResponseDto);
    }




    /**
     * SellingBook -> SellingBookResponseDto 변환
     */
    private SellingBookAndBookResponseDto toResponseDto(SellingBook sellingBook) {
        Book book = sellingBook.getBook();
        BookImage bookImage = bookImageRepository.findByBook(book).orElse(null);


        // 카테고리 정보 매핑
        List<String> categories = categoryRepository.findCategoriesByBookId(book.getBookId())
                .stream()
                .map(Category::getCategoryName)
                .toList();

        // 작가 정보 매핑
        List<String> authors = bookAuthorRepository.findAuthorsByBookId(book.getBookId())
                .stream()
                .map(Author::getAuthorName) // Author의 authorName을 가져옴
                .toList();

        // 출판사 정보 가져오기
        String publisher = book.getPublisher().getPublisherName();

        return new SellingBookAndBookResponseDto(
                sellingBook.getSellingBookId(),
                book.getBookId(),
                sellingBook.getBookTitle(),
                sellingBook.getSellingBookPrice(),
                sellingBook.getSellingBookPackageable(),
                sellingBook.getSellingBookStock(),
                sellingBook.getSellingBookStatus(),
                sellingBook.getUsed(),
                sellingBook.getSellingBookViewCount(),
                bookImage != null ? bookImage.getImageUrl() : null,
                publisher,
                categories,
                authors
        );
    }
}
