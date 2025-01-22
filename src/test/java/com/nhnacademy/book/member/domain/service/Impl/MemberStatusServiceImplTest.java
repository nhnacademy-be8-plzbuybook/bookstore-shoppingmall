package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberStateException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MemberStatusServiceImplTest {

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberStatusServiceImpl memberStatusService;

    @Test
    @DisplayName("회원 상태를 성공적으로 저장할 때")
    void createMemberStatus_SuccessfullySavesState() {
        // given
        String stateName = "ACTIVE";
        MemberStatusCreateRequestDto memberStatusCreateRequestDto = new MemberStatusCreateRequestDto(stateName);

        //여기서는 정상 등록 테스트이므로 비어 있어야 함
        when(memberStatusRepository.findByMemberStateName(stateName)).thenReturn(Optional.empty());

        //저장
        when(memberStatusRepository.save(any(MemberStatus.class))).thenAnswer(invocation -> {
            MemberStatus savedStatus = invocation.getArgument(0);
            savedStatus.setMemberStateId(1L); // 저장 후 ID 설정
            return savedStatus;
        });

        // when
        MemberStatus savedStatus = memberStatusService.createMemberStatus(memberStatusCreateRequestDto);

        // then
        assertEquals(1L, savedStatus.getMemberStateId());
        assertEquals(stateName, savedStatus.getMemberStateName());

        // verify
        verify(memberStatusRepository, times(1)).findByMemberStateName(stateName);
        verify(memberStatusRepository, times(1)).save(any(MemberStatus.class));
    }

    @Test
    @DisplayName("중복된 회원 상태를 추가하려할 때 예외")
    void createMemberStatus_ThrowsExceptionWhenDuplicateStateExists() {
        // given
        String stateName = "ACTIVE";
        MemberStatusCreateRequestDto memberStatusCreateRequestDto = new MemberStatusCreateRequestDto(stateName);

        MemberStatus existingMemberStatus = new MemberStatus();
        existingMemberStatus.setMemberStateId(1L);
        existingMemberStatus.setMemberStateName(stateName);

        //여기서는 이미 있는 값을 저장하려고 하는 것이므로 존재 하는 값이 있게 함
        when(memberStatusRepository.findByMemberStateName(stateName)).thenReturn(Optional.of(existingMemberStatus));

        // when & then
        assertThrows(DuplicateMemberStateException.class,
                () -> memberStatusService.createMemberStatus(memberStatusCreateRequestDto));

        // verify
        verify(memberStatusRepository, times(1)).findByMemberStateName(stateName);
        //중복 상태일 때는 절대 저장되면 안되므로 never로 함
        verify(memberStatusRepository, never()).save(any(MemberStatus.class));
    }

    @Test
    @DisplayName("회원 상태 변경 성공")
    void updateMemberStatus_success() {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus dormantStatus = new MemberStatus(2L, "DORMANT");
        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, dormantStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(memberStatusRepository.findByMemberStateName("ACTIVE")).thenReturn(Optional.of(activeStatus));

        memberStatusService.updateMemberStatusActiveByEmail(member.getEmail());

        assertEquals("ACTIVE", member.getMemberStatus().getMemberStateName());
        verify(memberRepository).save(any(Member.class));

    }

    @Test
    @DisplayName("전체 상태 가져오니")
    void getAllMemberStatuses_Success() {
        MemberStatus dormantStatus = new MemberStatus(1L, "DORMANT");
        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");

        List<MemberStatus> memberStatuses = List.of(dormantStatus, activeStatus);
        when(memberStatusRepository.findAll()).thenReturn(memberStatuses);
        List<MemberStatus> actual = memberStatusService.getAllMemberStatuses();
        assertEquals(memberStatuses, actual);
    }
}