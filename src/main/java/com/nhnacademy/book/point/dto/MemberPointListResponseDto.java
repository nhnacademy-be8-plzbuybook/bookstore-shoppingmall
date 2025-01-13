package com.nhnacademy.book.point.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointListResponseDto {
    @NotNull
    private Long memberPointId;
    @NotNull
    private Long memberId;
    private String name;
    private Integer point;
    private LocalDateTime addDate;
    private LocalDateTime endDate;
    private LocalDateTime usingDate;
    private String type;

}
