package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberStateException;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MemberStatusServiceImplTest {

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @InjectMocks
    private MemberStatusServiceImpl memberStatusService;

    @Test
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
}