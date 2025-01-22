package com.nhnacademy.book.point.repository;

import com.nhnacademy.book.point.domain.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {
    List<MemberPoint> findByMember_email(String email);

    // 날짜 내림차순으로 정렬하기 위함
    @Query("SELECT p FROM MemberPoint p WHERE p.member.memberId = :memberId " +
            "ORDER BY COALESCE(p.addDate, p.usingDate) DESC")
    List<MemberPoint> findAllByMemberIdOrderByDateDesc(Long memberId);



}
