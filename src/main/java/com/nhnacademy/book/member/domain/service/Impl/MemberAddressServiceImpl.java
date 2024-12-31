package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAddress;
import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberAddressResponseDto;
import com.nhnacademy.book.member.domain.exception.AddressLimitExceededException;
import com.nhnacademy.book.member.domain.exception.DuplicateAddressException;
import com.nhnacademy.book.member.domain.exception.MemberEmailNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberAddressRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.MemberAddressService;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberAddressServiceImpl implements MemberAddressService {
    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberAddressResponseDto addAddress(Long memberId, MemberAddressRequestDto addressRequestDto) {
        // 회원 존재 여부 확인
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        List<MemberAddress> existingAddresses = memberAddressRepository.findByMember_memberId(memberId);
        if (existingAddresses.size() >= 10) {
            throw new AddressLimitExceededException("회원은 최대 10개의 주소를 등록할 수 있습니다.");
        }

        // 주소 중복 확인 (도로명 주소와 상세주소가 같을때 중복 처리)
        Optional<MemberAddress> existingAddress = memberAddressRepository.findByLocationAddressAndDetailAddressAndMember_memberId(
                addressRequestDto.getLocationAddress(), addressRequestDto.getDetailAddress(), memberId);
        if (existingAddress.isPresent()) {
            throw new DuplicateAddressException("해당 주소는 이미 등록되어 있습니다.");
        }

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setLocationAddress(addressRequestDto.getLocationAddress());
        memberAddress.setDetailAddress(addressRequestDto.getDetailAddress());
        memberAddress.setZipCode(addressRequestDto.getZipCode());
        memberAddress.setNickName(addressRequestDto.getNickName());
        memberAddress.setRecipient(addressRequestDto.getRecipient());
        memberAddress.setRecipientPhone(addressRequestDto.getRecipientPhone());

        if (existingAddresses.isEmpty()) {
            memberAddress.setDefaultAddress(true);
        } else {
            memberAddress.setDefaultAddress(false);
        }

        memberAddress.setMember(memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다.")));


        MemberAddress savedAddress = memberAddressRepository.save(memberAddress);

        return new MemberAddressResponseDto(
                savedAddress.getMemberAddressId(),
                savedAddress.getDefaultAddress(),
                savedAddress.getLocationAddress(),
                savedAddress.getDetailAddress(),
                savedAddress.getZipCode(),
                savedAddress.getNickName(),
                savedAddress.getRecipient(),
                savedAddress.getRecipientPhone()
        );

    }

    @Override
    public MemberAddressResponseDto createAddress(String email, MemberAddressRequestDto addressRequestDto) {
        // 회원 존재 여부 확인
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberEmailNotFoundException("email에 해당하는 회원이 존재하지 않습니다."));

        List<MemberAddress> existingAddresses = memberAddressRepository.findByMember_memberId(member.getMemberId());
        if (existingAddresses.size() >= 10) {
            throw new AddressLimitExceededException("회원은 최대 10개의 주소를 등록할 수 있습니다.");
        }

        // 주소 중복 확인 (도로명 주소와 상세주소가 같을때 중복 처리)
        Optional<MemberAddress> existingAddress = memberAddressRepository.findByLocationAddressAndDetailAddressAndMember_memberId(
                addressRequestDto.getLocationAddress(), addressRequestDto.getDetailAddress(), member.getMemberId());
        if (existingAddress.isPresent()) {
            throw new DuplicateAddressException("해당 주소는 이미 등록되어 있습니다.");
        }

        // 새로운 주소 생성
        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setMember(member);
        memberAddress.setLocationAddress(addressRequestDto.getLocationAddress());
        memberAddress.setDetailAddress(addressRequestDto.getDetailAddress());
        memberAddress.setZipCode(addressRequestDto.getZipCode());
        memberAddress.setNickName(addressRequestDto.getNickName());
        memberAddress.setRecipient(addressRequestDto.getRecipient());
        memberAddress.setRecipientPhone(addressRequestDto.getRecipientPhone());

        // 기본 배송지 설정
        if (addressRequestDto.getDefaultAddress() || existingAddresses.isEmpty()) {
            // 기존 기본 배송지 해제
            for (MemberAddress existingAddressItem : existingAddresses) {
                if (existingAddressItem.getDefaultAddress()) {
                    existingAddressItem.setDefaultAddress(false);
                    memberAddressRepository.save(existingAddressItem);
                }
            }
            memberAddress.setDefaultAddress(true);
        } else {
            memberAddress.setDefaultAddress(false);
        }

        MemberAddress savedAddress = memberAddressRepository.save(memberAddress);

        return new MemberAddressResponseDto(
                savedAddress.getMemberAddressId(),
                savedAddress.getDefaultAddress(),
                savedAddress.getLocationAddress(),
                savedAddress.getDetailAddress(),
                savedAddress.getZipCode(),
                savedAddress.getNickName(),
                savedAddress.getRecipient(),
                savedAddress.getRecipientPhone()
        );
    }

    @Override
    // 배송지 목록 조회
    public List<MemberAddressResponseDto> getAddressList(Long memberId) {

        memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        List<MemberAddress> memberAddresses = memberAddressRepository.findByMember_memberId(memberId);

        return memberAddresses.stream()
                .map(memberAddress -> new MemberAddressResponseDto(
                        memberAddress.getMemberAddressId(),
                        memberAddress.getDefaultAddress(),
                        memberAddress.getLocationAddress(),
                        memberAddress.getDetailAddress(),
                        memberAddress.getZipCode(),
                        memberAddress.getNickName(),
                        memberAddress.getRecipient(),
                        memberAddress.getRecipientPhone()
                ))
                .collect((Collectors.toList()));

    }

    @Override
    public List<MemberAddressResponseDto> getAddressListByMemberEmail(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberEmailNotFoundException("이메일에 해당하는 회원이 없다!"));

        List<MemberAddress> memberAddresses = memberAddressRepository.findByMember_memberId(member.getMemberId());

        return memberAddresses.stream()
                .map(memberAddress -> new MemberAddressResponseDto(
                        memberAddress.getMemberAddressId(),
                        memberAddress.getDefaultAddress(),
                        memberAddress.getLocationAddress(),
                        memberAddress.getDetailAddress(),
                        memberAddress.getZipCode(),
                        memberAddress.getNickName(),
                        memberAddress.getRecipient(),
                        memberAddress.getRecipientPhone()
                ))
                .collect((Collectors.toList()));
    }

    @Override
    // 배송지 상세 조회
    public MemberAddressResponseDto getAddress(Long memberId, Long addressId) {

        memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        MemberAddress memberAddress = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소는 존재하지 않습니다."));

        return new MemberAddressResponseDto(
                memberAddress.getMemberAddressId(),
                memberAddress.getDefaultAddress(),
                memberAddress.getLocationAddress(),
                memberAddress.getDetailAddress(),
                memberAddress.getZipCode(),
                memberAddress.getNickName(),
                memberAddress.getRecipient(),
                memberAddress.getRecipientPhone()
        );


    }

    @Transactional
    @Override
    public MemberAddressResponseDto updateAddress(Long memberId, Long addressId, MemberAddressRequestDto addressRequestDto) {

        memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        MemberAddress existingAddress = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소는 존재하지 않습니다."));

        existingAddress.setLocationAddress(addressRequestDto.getLocationAddress());
        existingAddress.setDetailAddress(addressRequestDto.getDetailAddress());
        existingAddress.setZipCode(addressRequestDto.getZipCode());
        existingAddress.setNickName(addressRequestDto.getNickName());
        existingAddress.setRecipient(addressRequestDto.getRecipient());
        existingAddress.setRecipientPhone(addressRequestDto.getRecipientPhone());

        if (addressRequestDto.getDefaultAddress() != null && addressRequestDto.getDefaultAddress()) {
            memberAddressRepository.findByMember_memberId(memberId).stream()
                    .filter(memberAddress -> memberAddress.getDefaultAddress())
                    .forEach(memberAddress -> memberAddress.setDefaultAddress(false));
            existingAddress.setDefaultAddress(true);
        }

        MemberAddress updateAddress = memberAddressRepository.save(existingAddress);

        return new MemberAddressResponseDto(
                updateAddress.getMemberAddressId(),
                updateAddress.getDefaultAddress(),
                updateAddress.getLocationAddress(),
                updateAddress.getDetailAddress(),
                updateAddress.getZipCode(),
                updateAddress.getNickName(),
                updateAddress.getRecipient(),
                updateAddress.getRecipientPhone()
        );


    }
    @Override
    public void deleteAddress(Long memberId, Long addressId) {
        memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        MemberAddress existingAddress = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소는 존재하지 않습니다."));

    // 주소가 기본 주소일 경우 기본주소를 다른 주소로 설정해야함
    if (existingAddress.getDefaultAddress()) {
        List<MemberAddress> memberAddress = memberAddressRepository.findByMember_memberId(memberId);

        if (!memberAddress.isEmpty()) {
            // 첫번째 주소를 기본 주소로 설정
            MemberAddress newDefaultAddress = memberAddress.stream()
                    .filter(address -> !address.getDefaultAddress())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(("기본 주소로 설정할 다른 주소가 없습니다")));
                    newDefaultAddress.setDefaultAddress(true);
                    memberAddressRepository.save(newDefaultAddress);
        }
    }
    memberAddressRepository.delete(existingAddress);

    }

    @Override
    public void deleteAddressByEmail(String email, Long addressId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberEmailNotFoundException("email에 해당하는 회원이 없다!"));

        MemberAddress existingAddress = memberAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소는 존재하지 않습니다."));

        // 주소가 기본 주소일 경우 기본주소를 다른 주소로 설정해야함
        if (existingAddress.getDefaultAddress()) {
            List<MemberAddress> memberAddress = memberAddressRepository.findByMember_memberId(member.getMemberId());

            if (!memberAddress.isEmpty()) {
                // 첫번째 주소를 기본 주소로 설정
                MemberAddress newDefaultAddress = memberAddress.stream()
                        .filter(address -> !address.getDefaultAddress())
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(("기본 주소로 설정할 다른 주소가 없습니다")));
                newDefaultAddress.setDefaultAddress(true);
                memberAddressRepository.save(newDefaultAddress);
            }
        }
        memberAddressRepository.delete(existingAddress);

    }
}

