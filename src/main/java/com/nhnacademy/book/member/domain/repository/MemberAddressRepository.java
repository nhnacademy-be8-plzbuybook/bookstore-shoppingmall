package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
}
