package com.nhnacademy.book.member.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberEmailResponseDto {


    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    //권한
    @NotBlank
    @Size(max = 100)
    private String authName;

    //비밀번호
    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    private String memberStateName;

}