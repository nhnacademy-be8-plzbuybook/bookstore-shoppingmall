package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
    Optional<MemberGrade> findByMemberGradeName(String memberGradeName);
}
