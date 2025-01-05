package com.nhnacademy.book.member.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberModifyByAdminRequestDto {
    //이름전화번호 이메일 생년 월일 등급 상태

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 15)
    private String phone;

    @Email
    @Size(max = 100)
    private String email;

    private LocalDate birth;

    private Long memberGradeId;

    private Long memberStateId;
}
