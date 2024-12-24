package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.LikesDocument;
import com.nhnacademy.book.book.entity.Likes;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.member.domain.Member;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesSearchRepository extends ElasticsearchRepository<LikesDocument, Long> {

    // 특정 회원이 특정 책을 이미 좋아요했는지 확인
    boolean existsByMemberAndSellingBook(Member member, SellingBook sellingBook);
}
