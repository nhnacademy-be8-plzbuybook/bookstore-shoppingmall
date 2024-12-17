package com.nhnacademy.book.member.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberAddressRequestDto {
    private String locationAddress;
    private String detailAddress;
    private String zipCode;
    private String nickName;
    private String recipient;
    private String recipientPhone;
}
