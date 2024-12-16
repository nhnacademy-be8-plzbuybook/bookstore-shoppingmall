package com.nhnacademy.book.member.domain.repository.auth;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MemberAuthRepository extends JpaRepository<MemberAuth, Long> {
    List<MemberAuth> findByMember(Member member);
    Optional<MemberAuth> findByMemberAndAuth(Member member, Auth auth);
}


