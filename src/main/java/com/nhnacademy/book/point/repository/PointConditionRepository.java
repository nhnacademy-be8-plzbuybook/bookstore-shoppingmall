package com.nhnacademy.book.point.repository;

import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.domain.PointConditionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointConditionRepository extends JpaRepository<PointCondition, Long> {
    Optional<PointCondition> findByPointConditionName(PointConditionName pointConditionName);

}
