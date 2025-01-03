package com.nhnacademy.book.point.repository;

import com.nhnacademy.book.point.domain.MemberPoint;
import com.nhnacademy.book.point.domain.PointCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {
    List<MemberPoint> findAllByMember_MemberId(Long memberId);


}

