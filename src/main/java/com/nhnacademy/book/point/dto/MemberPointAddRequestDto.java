package com.nhnacademy.book.point.dto;

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
    private String name;
    private Integer conditionPoint;
    private BigDecimal conditionPercentage;


}
