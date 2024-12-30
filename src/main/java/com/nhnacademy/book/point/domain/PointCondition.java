package com.nhnacademy.book.point.domain;

import com.nhnacademy.book.converter.PointConditionNameConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long pointConditionId;


    @Convert(converter = PointConditionNameConverter.class)
    @Column(nullable = false)
    private PointConditionName name;

    // 회원가입시 5000p, 리뷰: 200p, 리뷰와 사진: 500
    @Column
    private Integer conditionPoint;

    // 도서구매시 기본 포인트 비율 5%
    @Column
    private BigDecimal conditionPercentage;
}
