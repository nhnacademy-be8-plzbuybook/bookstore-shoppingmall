package com.nhnacademy.book.member.domain.controller;//package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.*;
import com.nhnacademy.book.member.domain.exception.MemberGradeNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberIdNotFoundException;
import com.nhnacademy.book.member.domain.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @Test
    void createMember() {
        MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
        memberCreateRequestDto.setMemberGradeId(1L);
        memberCreateRequestDto.setMemberStateId(1L);
        memberCreateRequestDto.setName("윤지호");
        memberCreateRequestDto.setPhone("010-7237-3951");
        memberCreateRequestDto.setEmail("yoonwlgh12@naver.com");
        memberCreateRequestDto.setBirth(LocalDate.of(2000, 3,9));
        memberCreateRequestDto.setPassword("1111");

        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName("NORMAL");

        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStateName("ACTIVE");

        MemberCreateResponseDto memberCreateResponseDto = new MemberCreateResponseDto();
        memberCreateResponseDto.setName("윤지호");
        memberCreateResponseDto.setPhone("010-7237-3951");
        memberCreateResponseDto.setEmail("yoonwlgh12@naver.com");
        memberCreateResponseDto.setBirth(LocalDate.of(2000, 3,9));
        memberCreateResponseDto.setMemberGradeName(memberGrade.getMemberGradeName());
        memberCreateResponseDto.setMemberStateName(memberStatus.getMemberStateName());

        when(memberService.createMember(memberCreateRequestDto)).thenReturn(memberCreateResponseDto);

        ResponseEntity<MemberCreateResponseDto> responseEntity = memberController.createMember(memberCreateRequestDto);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(memberCreateResponseDto.getName(), responseEntity.getBody().getName());
        assertEquals(memberCreateResponseDto.getPhone(), responseEntity.getBody().getPhone());
        assertEquals(memberCreateResponseDto.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(memberCreateResponseDto.getBirth(), responseEntity.getBody().getBirth());
        assertEquals(memberCreateResponseDto.getMemberGradeName(), responseEntity.getBody().getMemberGradeName());
        assertEquals(memberCreateResponseDto.getMemberStateName(), responseEntity.getBody().getMemberStateName());


    }

    @Test
    void modifyMember() {
        MemberModifyRequestDto memberModifyRequestDto = new MemberModifyRequestDto();
        memberModifyRequestDto.setName("윤지호");
        memberModifyRequestDto.setPhone("010-7237-3951");
        memberModifyRequestDto.setEmail("yoonwlgh12@naver.com");
        memberModifyRequestDto.setBirth(LocalDate.of(2000, 3,9));
        memberModifyRequestDto.setPassword("1111");

        MemberModifyResponseDto memberModifyResponseDto = new MemberModifyResponseDto();
        memberModifyResponseDto.setName("yoonwlgh");
        memberModifyResponseDto.setPhone("010-1111-1111");
        memberModifyResponseDto.setEmail("yoonwlgh123@naver.com");
        memberModifyResponseDto.setBirth(LocalDate.of(2000, 3,10));

        when(memberService.modify(1L, memberModifyRequestDto)).thenReturn(memberModifyResponseDto);

        ResponseEntity<MemberModifyResponseDto> responseEntity = memberController.modifyMember(1L, memberModifyRequestDto);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(memberModifyResponseDto.getName(), responseEntity.getBody().getName());
        assertEquals(memberModifyResponseDto.getPhone(), responseEntity.getBody().getPhone());
        assertEquals(memberModifyResponseDto.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(memberModifyResponseDto.getBirth(), responseEntity.getBody().getBirth());
    }

    @Test
    void getMemberByEmail() {
        String email = "yoonwlgh12@naver.com";

        MemberEmailResponseDto memberEmailResponseDto = new MemberEmailResponseDto();
        memberEmailResponseDto.setEmail(email);
        memberEmailResponseDto.setPassword("1111");
        memberEmailResponseDto.setAuthName("ADMIN");


        when(memberService.getMemberByEmail(email)).thenReturn(memberEmailResponseDto);

        ResponseEntity<MemberEmailResponseDto> responseEntity = memberController.getMemberByEmail(email);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(memberEmailResponseDto.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(memberEmailResponseDto.getPassword(), responseEntity.getBody().getPassword());
        assertEquals(memberEmailResponseDto.getAuthName(), responseEntity.getBody().getAuthName());
    }

    @Test
    void getMemberById() {
        Long memberId = 1L;

        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName("NORMAL");

        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStateName("ACTIVE");

        MemberIdResponseDto memberIdResponseDto = new MemberIdResponseDto();
        memberIdResponseDto.setName("윤지호");
        memberIdResponseDto.setPhone("010-7237-3951");
        memberIdResponseDto.setEmail("yoonwlgh12@naver.com");
        memberIdResponseDto.setBirth(LocalDate.of(2000, 3,9));
        memberIdResponseDto.setMemberGradeName(memberGrade.getMemberGradeName());
        memberIdResponseDto.setMemberStateName(memberStatus.getMemberStateName());

        when(memberService.getMemberById(memberId)).thenReturn(memberIdResponseDto);

        ResponseEntity<MemberIdResponseDto> responseEntity = memberController.getMemberById(memberId);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(memberIdResponseDto.getName(), responseEntity.getBody().getName());
        assertEquals(memberIdResponseDto.getPhone(), responseEntity.getBody().getPhone());
        assertEquals(memberIdResponseDto.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(memberIdResponseDto.getBirth(), responseEntity.getBody().getBirth());
        assertEquals(memberIdResponseDto.getMemberGradeName(), responseEntity.getBody().getMemberGradeName());
        assertEquals(memberIdResponseDto.getMemberStateName(), responseEntity.getBody().getMemberStateName());
    }

    @Test
    void withdrawMember() {
        Long memberId = 1L;

        doNothing().when(memberService).withdrawMember(memberId);

        ResponseEntity<String> responseEntity = memberController.withdrawMember(memberId);

        verify(memberService, times(1)).withdrawMember(memberId);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("탈퇴 처리 됐습니다.", responseEntity.getBody());

    }

    @Test
    void withdrawMember_memberNotFound() {
        Long memberId = 1L;

        doThrow(new MemberIdNotFoundException("id에 해당하는 member가 없다!"))
                .when(memberService).withdrawMember(memberId);

        MemberIdNotFoundException exception = assertThrows(MemberIdNotFoundException.class, () ->
                memberController.withdrawMember(memberId));

        assertEquals("id에 해당하는 member가 없다!", exception.getMessage());

        verify(memberService, times(1)).withdrawMember(memberId);
    }

    @Test
    void withdrawMember_memberGradeNotFOundException() {
        Long memberId = 1L;

        doThrow(new MemberGradeNotFoundException("withdraw 상태가 없다!"))
                .when(memberService).withdrawMember(memberId);

        MemberGradeNotFoundException exception = assertThrows(MemberGradeNotFoundException.class, () ->
                memberController.withdrawMember(memberId));

        assertEquals("withdraw 상태가 없다!", exception.getMessage());

        verify(memberService, times(1)).withdrawMember(memberId);
    }

    @Test
    void getMembers() {
        MemberSearchRequestDto memberSearchRequestDto = new MemberSearchRequestDto();
        memberSearchRequestDto.setPage(0);
        memberSearchRequestDto.setSize(10);

        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName("NORMAL");

        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStateName("ACTIVE");


        MemberSearchResponseDto response1 = new MemberSearchResponseDto("윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.of(2000,3,9), memberGrade.getMemberGradeName(), memberStatus.getMemberStateName());
        MemberSearchResponseDto response2 = new MemberSearchResponseDto("윤지호2", "010-7237-3952", "yoonwlgh123@naver.com", LocalDate.of(2001,3,9), memberGrade.getMemberGradeName(), memberStatus.getMemberStateName());

        Page<MemberSearchResponseDto> page = new PageImpl<>(Arrays.asList(response1, response2));

        when(memberService.getMembers(memberSearchRequestDto)).thenReturn(page);

        ResponseEntity<Page<MemberSearchResponseDto>> responseEntity = memberController.getMembers(memberSearchRequestDto);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getContent().size());
        assertEquals("윤지호", responseEntity.getBody().getContent().get(0).getName());
        assertEquals("윤지호2", responseEntity.getBody().getContent().get(1).getName());



    }
}

