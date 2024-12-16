package com.nhnacademy.book.member.domain.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberGradeCreateRequestDto {
    @NotNull
    @Size(max = 10)
    private String memberGradeName;

    @NotNull
    private BigDecimal conditionPrice;

    @NotNull
    private LocalDateTime gradeChange;
}
