package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAddress;
import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberAddressResponseDto;
import com.nhnacademy.book.member.domain.exception.AddressLimitExceededException;
import com.nhnacademy.book.member.domain.exception.DuplicateAddressException;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberStateException;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberAddressRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberAddressServiceImplTest {
    @Mock
    private MemberAddressRepository memberAddressRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberAddressServiceImpl memberAddressService;
    private MemberAddressRequestDto addressRequestDto;
    private Member member;

    @BeforeEach
    public void setUp() {
        addressRequestDto = new MemberAddressRequestDto();
        addressRequestDto.setLocationAddress("광주 동구 필문대로 309");
        addressRequestDto.setDetailAddress("IT융합대학 4225");
        addressRequestDto.setZipCode("64132");
        addressRequestDto.setNickName("학교");
        addressRequestDto.setRecipient("test");
        addressRequestDto.setRecipientPhone("010-1234-5679");


        member = new Member();
        member.setMemberId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
//        when(memberAddressRepository.findByMember_memberId(1L)).thenReturn(List.of());

    }

    @Test
    @DisplayName("정상적으로 주소가 추가되는지")
    void addAddress_Success() {

        MemberAddress savedAddress = new MemberAddress();
        savedAddress.setMemberAddressId(1L);
        savedAddress.setDefaultAddress(true);
        savedAddress.setLocationAddress(addressRequestDto.getLocationAddress());
        savedAddress.setDetailAddress(addressRequestDto.getDetailAddress());
        savedAddress.setZipCode(addressRequestDto.getZipCode());
        savedAddress.setNickName(addressRequestDto.getNickName());
        savedAddress.setRecipient(addressRequestDto.getRecipient());
        savedAddress.setRecipientPhone(addressRequestDto.getRecipientPhone());

        when(memberAddressRepository.save(any(MemberAddress.class))).thenReturn(savedAddress);

        MemberAddressResponseDto response = memberAddressService.addAddress(1L, addressRequestDto);

        assertNotNull(response);
        assertEquals(addressRequestDto.getLocationAddress(), response.getLocationAddress());
        assertEquals(addressRequestDto.getDetailAddress(), response.getDetailAddress());
        assertEquals(addressRequestDto.getZipCode(), response.getZipCode());
        assertEquals(addressRequestDto.getNickName(), response.getNickName());
        assertEquals(addressRequestDto.getRecipient(), response.getRecipient());
        assertEquals(addressRequestDto.getRecipientPhone(), response.getRecipientPhone());
        assertTrue(response.getDefaultAddress());  // 첫 번째 주소는 기본 주소로 설정되어야 함


    }
    @Test
    @DisplayName("회원이 존재하지 않을 때 예외 발생")
    void addAddress_MemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () ->  {
            memberAddressService.addAddress(1L, addressRequestDto);
        });
        assertEquals("회원이 존재하지 않습니다.", exception.getMessage());

    }

    @Test
    @DisplayName("10개의 주소가 존재할 때 예외 발생")
    void addAddress_AddressLimitExceeded() {
        when(memberAddressRepository.findByMember_memberId(1L)).thenReturn(
                List.of(new MemberAddress(), new MemberAddress(), new MemberAddress(),
                        new MemberAddress(), new MemberAddress(), new MemberAddress(),
                        new MemberAddress(), new MemberAddress(), new MemberAddress(), new MemberAddress()) // 10개 주소
        );

        // when + then
        AddressLimitExceededException exception = assertThrows(AddressLimitExceededException.class, () -> {
            memberAddressService.addAddress(1L, addressRequestDto);
        });

        assertEquals("회원은 최대 10개의 주소를 등록할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("이미 같은 주소가 등록되어 있을 떄 예외 발생")
    void addAddress_DuplicateAddress() {
        MemberAddress existingAddress = new MemberAddress();
        existingAddress.setLocationAddress("광주 동구 필문대로 309");
        existingAddress.setDetailAddress("IT융합대학 4225");

        when(memberAddressRepository.findByLocationAddressAndDefaultAddressAndMember_memberId(
                addressRequestDto.getLocationAddress(),
                addressRequestDto.getDetailAddress(),
                1L))
                .thenReturn(Optional.of(existingAddress));

        DuplicateAddressException exception = assertThrows(DuplicateAddressException.class, () -> {
            memberAddressService.addAddress(1L, addressRequestDto);
        });

        assertEquals("해당 주소는 이미 등록되어 있습니다.", exception.getMessage());
    }



    }


