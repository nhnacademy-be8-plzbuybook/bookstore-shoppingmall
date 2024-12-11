package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.MemberCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCertificationRepository extends JpaRepository<MemberCertification, Long> {
}
