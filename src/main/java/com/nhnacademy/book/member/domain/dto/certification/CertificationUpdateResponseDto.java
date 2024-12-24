package com.nhnacademy.book.member.domain.dto.certification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificationUpdateResponseDto {
    private Long memberId;
    private String certification;
}
