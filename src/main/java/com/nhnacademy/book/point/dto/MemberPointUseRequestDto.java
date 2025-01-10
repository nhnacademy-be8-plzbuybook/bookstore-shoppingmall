package com.nhnacademy.book.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointUseRequestDto {
    private String email;
    private Integer usedPoint;
}
