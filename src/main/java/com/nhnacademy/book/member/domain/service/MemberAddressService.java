package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.MemberAddress;
import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberAddressResponseDto;

import java.util.List;

public interface MemberAddressService {
    MemberAddressResponseDto addAddress(Long memberId, MemberAddressRequestDto addressRequestDto);
    List<MemberAddressResponseDto> getAddressList(Long memberId);
    MemberAddressResponseDto getAddress(Long memberId, Long addressId);
    MemberAddressResponseDto updateAddress(Long memberId, Long addressId, MemberAddressRequestDto addressRequestDto);
    void deleteAddress(Long memberId, Long addressId);
}
