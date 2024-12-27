package com.nhnacademy.book.member.domain.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthResponseDto {
    @NotNull
    private Long memberId;
    @NotNull
    private Long authId;
}
