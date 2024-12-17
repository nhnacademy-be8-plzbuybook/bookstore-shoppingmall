package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.MemberAddress;
import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberAddressResponseDto;

public interface MemberAddressService {
    MemberAddressResponseDto addAddress(Long memberId, MemberAddressRequestDto addressRequestDto);
}
