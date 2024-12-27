package com.nhnacademy.book.member.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberIdResponseDto {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String phone;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    private LocalDate birth;

    @NotNull
    private String memberGradeName;

    @NotNull
    private String memberStateName;

//    private String role;
}
