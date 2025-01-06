package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.service.MemberService;
import com.nhnacademy.book.member.domain.service.MemberStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberStatusControllerTest {

    @Mock
    private MemberStatusService memberStatusService;

    @InjectMocks
    private MemberStatusController memberStatusController;

    @Test
    @DisplayName("회원 상태 생성 controller test")
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

    @Test
    @DisplayName("회원 상태 생성 controller test")
    void updateMemberStatus() {
        // Given
        String email = "yoonwlgh12@naver.com";

        // Mock 설정: 서비스 호출 시 아무 동작도 하지 않도록 설정
        doNothing().when(memberStatusService).updateMemberStatusActiveByEmail(email);

        // When
        ResponseEntity<String> response = memberStatusController.activateMemberStatus(email);

        // Then
        assertNotNull(response); // 응답이 null이 아님을 확인
        assertEquals(HttpStatus.OK, response.getStatusCode()); // 상태 코드 검증
        assertEquals("회원 상태가 active로 변경!", response.getBody()); // 응답 메시지 검증
    }

    @Test
    @DisplayName("회원 상태 업데이트 실패 Controller Test")
    void activateMemberStatus_Failure() {
        // Given
        String email = "nonexistent@example.com";
        String errorMessage = "회원을 찾을 수 없습니다.";

        // Mock 설정: 실패 케이스
        doThrow(new MemberNotFoundException(errorMessage))
                .when(memberStatusService).updateMemberStatusActiveByEmail(email);

        // When
        ResponseEntity<String> response = memberStatusController.activateMemberStatus(email);

        // Then
        assertNotNull(response); // 응답이 null이 아님을 확인
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // 상태 코드 검증
        assertTrue(response.getBody().contains("회원 상태 변경 실패")); // 응답 메시지 검증 (기본 메시지 포함)
        assertTrue(response.getBody().contains(errorMessage)); // 응답 메시지 검증 (예외 메시지 포함)
    }

    @Test
    @DisplayName("회원 상태 전체 조회 controller test")
    void getAllMemberStatus() {
        // Given: 테스트 데이터 준비
        MemberStatus status1 = new MemberStatus();
        status1.setMemberStateId(1L);
        status1.setMemberStateName("ACTIVE");

        MemberStatus status2 = new MemberStatus();
        status2.setMemberStateId(2L);
        status2.setMemberStateName("INACTIVE");

        List<MemberStatus> expectedStatuses = List.of(status1, status2);

        // Mock 설정: 서비스에서 반환될 데이터 설정
        when(memberStatusService.getAllMemberStatuses()).thenReturn(expectedStatuses);

        // When: 컨트롤러 호출
        List<MemberStatus> response = memberStatusController.getAllMemberStatus();

        // Then: 결과 검증
        assertNotNull(response); // 결과가 null이 아님을 검증
        assertEquals(2, response.size()); // 리스트 크기 검증
        assertEquals("ACTIVE", response.get(0).getMemberStateName()); // 첫 번째 상태 이름 검증
        assertEquals("INACTIVE", response.get(1).getMemberStateName()); // 두 번째 상태 이름 검증
    }
}