package com.nhnacademy.book.point.dto;


import com.nhnacademy.book.point.domain.PointConditionName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointAddResponseDto {
    @NotNull
    private Long memberPointId;
    @NotNull
    private Long memberId;
    private String pointConditionName;
    private BigDecimal point;
    private LocalDateTime addDate;
    private LocalDateTime endDate;

}
