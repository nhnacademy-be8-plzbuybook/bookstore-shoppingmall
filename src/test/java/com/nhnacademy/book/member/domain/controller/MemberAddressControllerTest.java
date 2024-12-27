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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @BeforeEach
    void setup() {
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
    }

    @Test
    @DisplayName("배송지 등록 성공")
    void addAddress() throws Exception {
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
    @DisplayName("배송지 목록 조회 성공")
    void getAddressList() throws Exception {
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
    @DisplayName("배송지 상세 조회 성공")
    void getAddress_Success() throws Exception {
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
        // given
        Long memberId = 1L;
        Long addressId = 1L;

        doNothing().when(memberAddressService).deleteAddress(eq(memberId), eq(addressId));

        // when
        mockMvc.perform(delete("/api/members/" + memberId + "/address/" + addressId))
                .andExpect(status().isNoContent());
    }

}





