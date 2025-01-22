package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.MemberCertification;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberCertificationRepository extends JpaRepository<MemberCertification, Long> {
    Optional<MemberCertification> findByMember_MemberId(Long memberId);
    boolean existsByCertificationMethod(String certificationMethod);

    // 3개월 이상 미로그인 회원 조회
    @Query("SELECT mc FROM MemberCertification mc " + "JOIN FETCH mc.member m " +
            "WHERE mc.lastLogin <= :threeMonthsAgo " + "AND m.memberStatus.memberStateName = 'ACTIVE'")
    List<MemberCertification> findInactiveMember(@Param("threeMonthsAgo")LocalDateTime threeMonthsAgo);
}
