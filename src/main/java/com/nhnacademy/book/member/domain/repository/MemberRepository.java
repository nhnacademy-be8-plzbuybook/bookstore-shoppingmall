package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.Member;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhone(String phone);
    @Query("SELECT m FROM Member m JOIN FETCH m.memberGrade JOIN FETCH m.memberStatus WHERE m.email = :email")
    Optional<Member> findByEmailWithGradeAndStatus(@Param("email") String email);

    @Query("SELECT m FROM Member m WHERE FUNCTION('MONTH', m.birth) = :month")
    Page<Member> findByBirthMonth(@Param("month") int month, Pageable pageable); // 월별로 생일인 유저 조회 기능
  
    boolean existsByPhone(String phone);


    @Query("SELECT m.memberId FROM Member m WHERE m.email = :email")
    Long getMemberIdByEmail(String email);
}
 

