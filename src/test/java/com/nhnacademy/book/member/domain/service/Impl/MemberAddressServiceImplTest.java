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

import java.util.Arrays;
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
    @DisplayName("주소 추가_성공")
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
    @DisplayName("주소 추가_회원이 존재하지 않을 때")
    void addAddress_MemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            memberAddressService.addAddress(1L, addressRequestDto);
        });
        assertEquals("회원이 존재하지 않습니다.", exception.getMessage());

    }

    @Test
    @DisplayName("주소 추가_10개의 주소가 존재할 때")
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
    @DisplayName("주소 추가_이미 같은 주소가 등록되어 있을 떄")
    void addAddress_DuplicateAddress() {
        MemberAddress existingAddress = new MemberAddress();
        existingAddress.setLocationAddress("광주 동구 필문대로 309");
        existingAddress.setDetailAddress("IT융합대학 4225");

        when(memberAddressRepository.findByLocationAddressAndDetailAddressAndMember_memberId(
                addressRequestDto.getLocationAddress(),
                addressRequestDto.getDetailAddress(),
                1L))
                .thenReturn(Optional.of(existingAddress));

        DuplicateAddressException exception = assertThrows(DuplicateAddressException.class, () -> {
            memberAddressService.addAddress(1L, addressRequestDto);
        });

        assertEquals("해당 주소는 이미 등록되어 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주소 목록 조회_성공")
    void getAddressList_Success() {
        Long memberId = 1L;
        MemberAddress address1 = new MemberAddress();
        address1.setMemberAddressId(1L);
        address1.setLocationAddress("광주 동구 필문대로 309");
        address1.setDefaultAddress(true);
        address1.setDetailAddress("IT융합대학 4225");
        address1.setNickName("학교");
        address1.setZipCode("64132");
        address1.setRecipient("test");
        address1.setRecipientPhone("010-1234-5679");

        MemberAddress address2 = new MemberAddress();
        address2.setMemberAddressId(2L);
        address2.setLocationAddress("서울특별시 강남구 테헤란로 152");
        address2.setDefaultAddress(false);
        address2.setDetailAddress("강남빌딩 3층");
        address2.setNickName("회사");
        address2.setZipCode("06236");
        address2.setRecipient("test2");
        address2.setRecipientPhone("010-2345-6789");

        // 회원 존재 여부 mock
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));

        // 주소 목록 조회 mock
        when(memberAddressRepository.findByMember_memberId(memberId)).thenReturn(Arrays.asList(address1, address2));

        // when
        var result = memberAddressService.getAddressList(memberId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("광주 동구 필문대로 309", result.get(0).getLocationAddress());
        assertEquals("회사", result.get(1).getNickName());
    }

    @Test
    @DisplayName("주소 목록 조회_회원이 존재하지 않을 때")
    void getAddressList_MemberNotFound() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            memberAddressService.getAddressList(memberId);
        });

        assertEquals("회원이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주소 상세 조회_성공")
    void getAddress_Success() {
        Long memberId = 1L;
        Long addressId = 1L;

        MemberAddress address = new MemberAddress();
        address.setMemberAddressId(addressId);
        address.setLocationAddress("광주 동구 필문대로 309");
        address.setDefaultAddress(true);
        address.setDetailAddress("IT융합대학 4225");
        address.setNickName("학교");
        address.setZipCode("64132");
        address.setRecipient("test");
        address.setRecipientPhone("010-1234-5679");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(memberAddressRepository.findById(addressId)).thenReturn(Optional.of(address));
        var result = memberAddressService.getAddress(memberId, addressId);

        assertNotNull(result);
        assertEquals(addressId, result.getMemberAddressId());
        assertEquals("광주 동구 필문대로 309", result.getLocationAddress());
        assertEquals("학교", result.getNickName());
    }

    @Test
    @DisplayName("주소 상세 조회_회원이 존재하지 않을 때")
    void getAddress_MemberNotFound() {
        Long memberId = 1L;
        Long addressId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            memberAddressService.getAddress(memberId, addressId);
        });

        assertEquals("회원이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주소 상세 조회_주소가 존재하지 않을 떄")
    void getAddress_IllegalArgumentException() {
        Long memberId = 1L;
        Long addressId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(memberAddressRepository.findById(addressId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberAddressService.getAddress(memberId, addressId);
        });

        assertEquals("해당 주소는 존재하지 않습니다.", exception.getMessage());
    }


}







