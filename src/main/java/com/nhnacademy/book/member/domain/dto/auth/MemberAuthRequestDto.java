package com.nhnacademy.book.member.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthRequestDto {
    private Long memberId;
    private Long authId;

}
