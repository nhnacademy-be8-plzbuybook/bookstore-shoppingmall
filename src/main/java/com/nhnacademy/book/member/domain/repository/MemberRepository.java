package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    @Query("SELECT m FROM Member m JOIN FETCH m.memberGrade JOIN FETCH m.memberStatus WHERE m.email = :email")
    Optional<Member> findByEmailWithGradeAndStatus(@Param("email") String email);
}
 

