package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {
    Optional<MemberStatus> findByMemberStateName(String name);
}
