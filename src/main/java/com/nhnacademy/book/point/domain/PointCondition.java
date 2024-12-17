package com.nhnacademy.book.point.domain;

import com.nhnacademy.book.converter.PointConditionNameConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointConditionName name;

    @Column
    private Integer conditionPoint;

    @Column
    private Integer conditionPercentage;
}
