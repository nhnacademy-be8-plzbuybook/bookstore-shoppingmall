package com.nhnacademy.book.member.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberAddressRequestDto {
    private Boolean defaultAddress;
    @NotBlank
    private String locationAddress;
    @NotBlank
    private String detailAddress;
    @NotBlank
    private String zipCode;
    private String nickName;
    private String recipient;
    private String recipientPhone;
}
