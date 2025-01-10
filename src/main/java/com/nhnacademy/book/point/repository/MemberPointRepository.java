package com.nhnacademy.book.point.repository;

import com.nhnacademy.book.point.domain.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {
    List<MemberPoint> findAllByMember_MemberId(Long memberId);
    List<MemberPoint> findByMember_email(String email);


}
