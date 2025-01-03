package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Likes;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    // 특정 회원이 특정 책을 이미 좋아요했는지 확인
    boolean existsByMemberAndSellingBook(Member member, SellingBook sellingBook);

    // 특정 판매책에 대한 좋아요 수 조회 -> 쿼리 sdl 로 바꾸기..!!!!!!!!!!!!!!!!!
    @Query("SELECT COUNT(l) FROM Likes l WHERE l.sellingBook.sellingBookId = :sellingBookId")
    long countBySellingBookId(@Param("sellingBookId") Long sellingBookId);

    Optional<Likes> findByMemberAndSellingBook(Member member, SellingBook sellingBook);

    long countBySellingBook(SellingBook sellingBook);
}
