package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.member.domain.service.MemberStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberStatusControllerTest {

    @Mock
    private MemberStatusService memberStatusService;

    @InjectMocks
    private MemberStatusController memberStatusController;

    @Test
    void createMemberStatus() {
        MemberStatusCreateRequestDto memberStatusCreateRequestDto = new MemberStatusCreateRequestDto();
        memberStatusCreateRequestDto.setMemberStateName("ACTIVE");

        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStateId(1L);
        memberStatus.setMemberStateName(memberStatusCreateRequestDto.getMemberStateName());

        when(memberStatusService.createMemberStatus(memberStatusCreateRequestDto)).thenReturn(memberStatus);

        MemberStatus responseMemberStatus = memberStatusController.createMemberStatus(memberStatusCreateRequestDto);

        assertNotNull(responseMemberStatus);
        assertEquals(memberStatus.getMemberStateId(), responseMemberStatus.getMemberStateId());
        assertEquals(memberStatus.getMemberStateName(), responseMemberStatus.getMemberStateName());



    }
}