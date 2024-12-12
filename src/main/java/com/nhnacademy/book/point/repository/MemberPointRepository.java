package com.nhnacademy.book.point.repository;

import com.nhnacademy.book.point.domain.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {
}
