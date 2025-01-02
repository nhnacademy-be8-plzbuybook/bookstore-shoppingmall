package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.MemberAddress;
import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberAddressResponseDto;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface MemberAddressService {
    MemberAddressResponseDto addAddress(Long memberId, MemberAddressRequestDto addressRequestDto);
    MemberAddressResponseDto createAddress(String email, MemberAddressRequestDto addressRequestDto);
    List<MemberAddressResponseDto> getAddressList(Long memberId);
    List<MemberAddressResponseDto> getAddressListByMemberEmail(String email);
    MemberAddressResponseDto getAddress(Long memberId, Long addressId);
    MemberAddressResponseDto updateAddress(Long memberId, Long addressId, MemberAddressRequestDto addressRequestDto);
    MemberAddressResponseDto updateAddressByEmail(String email, Long addressId, MemberAddressRequestDto addressRequestDto);
    void deleteAddress(Long memberId, Long addressId);
    void deleteAddressByEmail(String email, Long addressId);
}
