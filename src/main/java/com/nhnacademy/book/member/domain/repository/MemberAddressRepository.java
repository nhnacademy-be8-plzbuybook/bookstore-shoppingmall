package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    List<MemberAddress> findByMemberId(Long memberId);
    Optional<MemberAddress> findByLocationAddressAndMemberId(String locationAddress, Long memberId);

}
