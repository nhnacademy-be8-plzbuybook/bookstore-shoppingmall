package com.nhnacademy.book.point.dto;

import com.nhnacademy.book.point.domain.PointConditionName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointAddRequestDto {
    @NotNull
    private Long memberId;
    private Long reviewId;
    private PointConditionName name;
    private Integer conditionPoint;
    private BigDecimal conditionPercentage;

}
