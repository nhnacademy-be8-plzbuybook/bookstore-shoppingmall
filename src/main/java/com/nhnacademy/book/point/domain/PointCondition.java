package com.nhnacademy.book.point.domain;

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
@Table(name = "point_condition")
public class PointCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long pointConditionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointConditionName name;

    @Column
    private Integer conditionPoint;

    @Column
    private Integer conditionPercentage;
}
