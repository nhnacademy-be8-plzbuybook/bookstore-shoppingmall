package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
}
