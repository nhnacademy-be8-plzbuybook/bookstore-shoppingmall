package com.nhnacademy.book.member.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.handler.GlobalExceptionHandler;
import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberAddressResponseDto;
import com.nhnacademy.book.member.domain.service.MemberAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberAddressControllerTest {

    @Mock
    private MemberAddressService memberAddressService;
    @InjectMocks
    private MemberAddressController memberAddressController;


    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    private MemberAddressRequestDto addressRequestDto;
    private MemberAddressResponseDto addressResponseDto;


    @Test
    @DisplayName("배송지 등록 성공")
    void addAddress() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberAddressController).build();

        // 테스트용 데이터
        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );
        // given
        Long memberId = 1L;
        when(memberAddressService.addAddress(eq(memberId), any(MemberAddressRequestDto.class)))
                .thenReturn(addressResponseDto);

        // when
        mockMvc.perform(post("/api/members/" + memberId + "/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(addressResponseDto)));
    }

    @Test
    @DisplayName("배송지 등록 성공 (header email)")
    void createAddress() throws Exception {
        String email = "yoonwlgh12@naver.com";

        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        when(memberAddressService.createAddress(eq(email), eq(addressRequestDto))).thenReturn(addressResponseDto);

        ResponseEntity<MemberAddressResponseDto> response = memberAddressController.createAddress(email, addressRequestDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(true, addressResponseDto.getDefaultAddress());
        assertEquals("광주 동구 필문대로 309", addressResponseDto.getLocationAddress());
        assertEquals("IT융합대학 4225", addressResponseDto.getDetailAddress());
        assertEquals("61452", addressResponseDto.getZipCode());
        assertEquals("학교", addressResponseDto.getNickName());
        assertEquals("test1", addressResponseDto.getRecipient());
        assertEquals("010-1234-5678", addressResponseDto.getRecipientPhone());





    }
    @Test
    @DisplayName("배송지 목록 조회 성공")
    void getAddressList() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberAddressController).build();

        // 테스트용 데이터
        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );
        Long memberId = 1L;
        List<MemberAddressResponseDto> addressList = List.of(addressResponseDto);

        when(memberAddressService.getAddressList(eq(memberId)))
                .thenReturn(addressList);

        mockMvc.perform(get("/api/members/" + memberId + "/address")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(addressList)));

    }

    @Test
    @DisplayName("배송지 목록 조회 성공(header email)")
    void getAddressList_HeaderEmail() throws Exception {
        String email = "yoonwlgh12@naver.com";

        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        // mock 설정
        when(memberAddressService.getAddressListByMemberEmail(eq(email))).thenReturn(List.of(addressResponseDto));

        // ResponseEntity 검증
        List<MemberAddressResponseDto> response = memberAddressController.getAddressListByMemberEmail(email);

        // 반환된 데이터 검증
        assertNotNull(response);
        assertEquals(1, response.size());  // 리스트 크기 검증
        assertEquals("광주 동구 필문대로 309", response.get(0).getLocationAddress());  // 첫 번째 주소 검증
        assertEquals("학교", response.get(0).getNickName());  // 첫 번째 닉네임 검증
        assertEquals("010-1234-5678", response.get(0).getRecipientPhone());  // 첫 번째 전화번호 검증
    }

    @Test
    @DisplayName("배송지 상세 조회 성공")
    void getAddress_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberAddressController).build();

        // 테스트용 데이터
        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );
        Long memberId = 1L;
        Long addressId = 1L;

        when(memberAddressService.getAddress(eq(memberId), eq(addressId)))
                .thenReturn(addressResponseDto);

        mockMvc.perform(get("/api/members/" + memberId + "/address/" + addressId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(addressResponseDto)));

    }

    @Test
    @DisplayName("배송지 수정 성공")
    void updateAddress() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberAddressController).build();

        // 테스트용 데이터
        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );
        Long memberId = 1L;
        Long addressId = 1L;

        MemberAddressRequestDto updateRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "공과대학",
                "64133",
                "학교",
                "test",
                "010-1234-5678"
        );

        MemberAddressResponseDto updatedAddressResponseDto = new MemberAddressResponseDto(
                addressId,
                true,
                "광주 동구 필문대로 309",
                "공과대학",
                "64133",
                "학교",
                "test",
                "010-1234-5678"
        );

        when(memberAddressService.updateAddress(eq(memberId), eq(addressId), any(MemberAddressRequestDto.class)))
                .thenReturn(updatedAddressResponseDto);

        mockMvc.perform(put("/api/members/" + memberId + "/address/" + addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedAddressResponseDto)));

    }


    @Test
    @DisplayName("배송지 삭제 성공")
    void deleteAddress() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberAddressController).build();

        // 테스트용 데이터
        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );
        // given
        Long memberId = 1L;
        Long addressId = 1L;

        doNothing().when(memberAddressService).deleteAddress(eq(memberId), eq(addressId));

        // when
        mockMvc.perform(delete("/api/members/" + memberId + "/address/" + addressId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("배송지 삭제 성공(header email을 통해서)")
    void deleteAddressByEmail() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberAddressController).build();

        // 테스트용 데이터
        addressRequestDto = new MemberAddressRequestDto(
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        addressResponseDto = new MemberAddressResponseDto(
                1L,
                true,
                "광주 동구 필문대로 309",
                "IT융합대학 4225",
                "61452",
                "학교",
                "test1",
                "010-1234-5678"
        );

        String email = "yoonwlgh12@naver.com";
        Long addressId = 1L;

        // given
        doNothing().when(memberAddressService).deleteAddressByEmail(eq(email), eq(addressId));

        // when
        mockMvc.perform(delete("/api/members/address/{address_id}", addressId)
                        .header("X-USER-ID", email))
                .andExpect(status().isNoContent());

    }

}





