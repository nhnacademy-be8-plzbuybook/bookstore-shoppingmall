package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberAddressResponseDto;
import com.nhnacademy.book.member.domain.service.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    // 배송지 등록
    @PostMapping("/members/{member_id}/address")
    public ResponseEntity<MemberAddressResponseDto> addAddress(@PathVariable Long member_id, @RequestBody MemberAddressRequestDto addressRequestDto) {
        MemberAddressResponseDto addressResponseDto = memberAddressService.addAddress(member_id, addressRequestDto);
        return new ResponseEntity<>(addressResponseDto, HttpStatus.CREATED);
    }

    //배송지 등록(header email을 통해)
    @PostMapping("/members/address")
    public ResponseEntity<MemberAddressResponseDto> createAddress(@RequestHeader("X-USER-ID") String email, @RequestBody MemberAddressRequestDto addressRequestDto) {
        MemberAddressResponseDto addressResponseDto = memberAddressService.createAddress(email, addressRequestDto);
        return new ResponseEntity<>(addressResponseDto, HttpStatus.CREATED);
    }

    // 배송지 목록 조회
    @GetMapping("/members/{member_id}/address")
    public List<MemberAddressResponseDto> getAddressList(@PathVariable Long member_id) {
        return memberAddressService.getAddressList(member_id);
    }

    //배송지 목록 조회(header email을 통해)
    @GetMapping("/members/address")
    public List<MemberAddressResponseDto> getAddressListByMemberEmail(@RequestHeader("X-USER-ID") String email) {
        return memberAddressService.getAddressListByMemberEmail(email);
    }

    // 배송지 상세 조회
    @GetMapping("/members/{member_id}/address/{address_id}")
    public MemberAddressResponseDto getAddress(@PathVariable Long member_id, @PathVariable Long address_id) {
        return memberAddressService.getAddress(member_id, address_id);
    }

    // 배송지 수정
    @PutMapping("/members/{member_id}/address/{address_id}")
    public MemberAddressResponseDto updateAddress(@PathVariable Long member_id, @PathVariable Long address_id, @RequestBody MemberAddressRequestDto addressRequestDto) {
        return memberAddressService.updateAddress(member_id, address_id, addressRequestDto);
    }

    // 배송지 수정(header email을 통해)
    @PostMapping("/members/address/{address_id}")
    public MemberAddressResponseDto updateAddress(@RequestHeader ("X-USER-ID") String email,@PathVariable Long address_id, @RequestBody MemberAddressRequestDto addressRequestDto) {
        return memberAddressService.updateAddressByEmail(email, address_id, addressRequestDto);
    }

    // 배송지 삭제
    @DeleteMapping("/members/{member_id}/address/{address_id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long member_id, @PathVariable Long address_id) {
        memberAddressService.deleteAddress(member_id, address_id);
        return ResponseEntity.noContent().build();
    }

    //배송지 삭제(header email을 통해)
    @DeleteMapping("/members/address/{address_id}")
    public ResponseEntity<Void> deleteAddress(@RequestHeader("X-USER-ID") String email, @PathVariable Long address_id) {
        memberAddressService.deleteAddressByEmail(email, address_id);
        return ResponseEntity.noContent().build();
    }

}
