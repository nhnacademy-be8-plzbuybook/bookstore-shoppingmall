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

//    @Query("SELECT ma FROM MemberAuth ma WHERE ma.member.memberId = :memberId AND ma.auth.authId = :authId")
//    Optional<MemberAuth> findByMemberIdAndAuthId(Long memberId, Long authId);

    void deleteByMemberMemberId(Long memberId);
}


