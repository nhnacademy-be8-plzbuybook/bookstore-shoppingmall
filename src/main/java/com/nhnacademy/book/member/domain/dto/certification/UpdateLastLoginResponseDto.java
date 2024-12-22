package com.nhnacademy.book.member.domain.dto.certification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLastLoginResponseDto {
    private Long memberId;
    private LocalDateTime lastLogin;
}
