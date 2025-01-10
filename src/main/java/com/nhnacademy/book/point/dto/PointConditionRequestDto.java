package com.nhnacademy.book.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointConditionRequestDto {
    private String pointConditionName;
    private Integer conditionPoint;
    private BigDecimal conditionPercentage;
    private boolean status;


}
