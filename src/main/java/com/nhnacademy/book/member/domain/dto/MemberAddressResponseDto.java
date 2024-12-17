package com.nhnacademy.book.member.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberAddressResponseDto {
    @NotNull
    private Long memberAddressId;
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
