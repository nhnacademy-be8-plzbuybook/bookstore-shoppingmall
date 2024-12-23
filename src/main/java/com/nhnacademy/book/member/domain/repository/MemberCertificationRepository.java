package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCertificationRepository extends JpaRepository<MemberCertification, Long> {
    Optional<MemberCertification> findByMember_MemberId(Long memberId);
    boolean existsByCertificationMethod(String certificationMethod);
}
