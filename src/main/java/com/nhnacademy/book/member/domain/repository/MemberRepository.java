package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
