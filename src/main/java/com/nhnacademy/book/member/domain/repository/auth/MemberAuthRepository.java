package com.nhnacademy.book.member.domain.repository.auth;

import com.nhnacademy.book.member.domain.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberAuthRepository extends JpaRepository<MemberAuth, Long> {
    List<MemberAuth> findByMemberId(Long memberId);

}
