package com.nhnacademy.book.member.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class MemberModifyRequestDto {


    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 15)
    private String phone;


    @Email
    @Size(max = 100)
    private String email;

    private LocalDate birth;


    @Size(max = 100)
    private String password;

}
