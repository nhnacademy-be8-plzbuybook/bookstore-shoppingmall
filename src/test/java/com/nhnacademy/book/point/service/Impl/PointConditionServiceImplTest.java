package com.nhnacademy.book.point.service.Impl;

import com.nhnacademy.book.member.domain.exception.PointConditionNotFoundException;
import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.dto.PointConditionRequestDto;
import com.nhnacademy.book.point.dto.PointConditionResponseDto;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PointConditionServiceImplTest {

    @Mock
    private PointConditionRepository pointConditionRepository;

    @InjectMocks
    private PointConditionServiceImpl pointConditionService;

    private PointConditionRequestDto validRequestDto;
    private PointCondition pointCondition;

    @BeforeEach
    void setUp() {
        validRequestDto = new PointConditionRequestDto(
                "NEW_CONDITION",
                100,
                null,
                true
        );

        pointCondition = new PointCondition();
        pointCondition.setPointConditionId(1L);
        pointCondition.setName("NEW_CONDITION");
        pointCondition.setConditionPoint(100);
        pointCondition.setConditionPercentage(null);
        pointCondition.setStatus(true);
    }

    @Test
    @DisplayName("포인트 조건 생성 - 성공")
    void createPointCondition_Success() {
        when(pointConditionRepository.<PointCondition>save(any(PointCondition.class)))
                .thenAnswer(invocation -> {
                    PointCondition argument = invocation.getArgument(0);
                    argument.setPointConditionId(1L); // ID 설정
                    return argument;
                });

        PointConditionResponseDto responseDto = pointConditionService.createPointCondition(validRequestDto);

        assertNotNull(responseDto);
        assertEquals(Long.valueOf(1L), responseDto.getId());
        assertEquals("NEW_CONDITION", responseDto.getName());
        assertEquals(Integer.valueOf(100), responseDto.getConditionPoint());
        assertNull(responseDto.getConditionPercentage());
        assertTrue(responseDto.isStatus());

        verify(pointConditionRepository, times(1)).save(any(PointCondition.class));
    }

    @Test
    @DisplayName("포인트 조건 생성 - 실패 (포인트와 비율 모두 입력)")
    void createPointCondition_Fail_BothPointAndPercentageProvided() {
        PointConditionRequestDto requestDto = new PointConditionRequestDto(
                "TEST_CONDITION", 100, new BigDecimal("0.05"), true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> pointConditionService.createPointCondition(requestDto));

        assertEquals("포인트와 비율 중 하나만 입력할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("포인트 조건 생성 - 실패 (포인트와 비율 미입력)")
    void createPointCondition_Fail_NoPointOrPercentageProvided() {
        PointConditionRequestDto requestDto = new PointConditionRequestDto(
                "TEST_CONDITION", null, null, true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pointConditionService.createPointCondition(requestDto));

        assertEquals("포인트와 비율 중 하나를 입력해야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("포인트 조건 전체 조회 - 성공")
    void getAllPointConditions_Success() {
        when(pointConditionRepository.findAll()).thenReturn(List.of(pointCondition));

        List<PointConditionResponseDto> responseDtos = pointConditionService.getAllPointConditions();

        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals("NEW_CONDITION", responseDtos.get(0).getName());
        assertEquals(Integer.valueOf(100), responseDtos.get(0).getConditionPoint());

        verify(pointConditionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("포인트 조건 수정 - 성공")
    void updatePointCondition_Success() {
        PointConditionRequestDto updateDto = new PointConditionRequestDto(
                "UPDATED_CONDITION", null, new BigDecimal("10.0"), false);

        when(pointConditionRepository.findById(1L)).thenReturn(Optional.of(pointCondition));
        when(pointConditionRepository.save(any(PointCondition.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PointConditionResponseDto responseDto = pointConditionService.updatePointCondition(1L, updateDto);

        assertNotNull(responseDto);
        assertEquals("UPDATED_CONDITION", responseDto.getName());
        assertNull(responseDto.getConditionPoint());
        assertEquals(new BigDecimal("10.0"), responseDto.getConditionPercentage());
        assertFalse(responseDto.isStatus());

        verify(pointConditionRepository, times(1)).findById(1L);
        verify(pointConditionRepository, times(1)).save(any(PointCondition.class));
    }

    @Test
    @DisplayName("포인트 조건 수정 - 실패")
    void updatePointCondition_Fail_NotFound() {
        when(pointConditionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PointConditionNotFoundException.class,
                () -> pointConditionService.updatePointCondition(1L, validRequestDto));

        verify(pointConditionRepository, times(1)).findById(1L);
        verify(pointConditionRepository, never()).save(any(PointCondition.class));
    }
}