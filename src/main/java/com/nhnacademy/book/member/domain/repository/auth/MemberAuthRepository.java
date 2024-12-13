package com.nhnacademy.book.member.domain.repository.auth;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MemberAuthRepository extends JpaRepository<MemberAuth, Long> {
    List<MemberAuth> findByMemberMemberId(Long memberId);
    void deleteByMemberMemberId(Long memberId);
}


