package com.nhnacademy.book.point.service;

import com.nhnacademy.book.point.dto.PointConditionRequestDto;
import com.nhnacademy.book.point.dto.PointConditionResponseDto;

import java.util.List;

public interface PointConditionService {
    PointConditionResponseDto createPointCondition(PointConditionRequestDto pointConditionRequestDto);
    List<PointConditionResponseDto> getAllPointConditions();
    PointConditionResponseDto updatePointCondition(Long id, PointConditionRequestDto pointConditionRequestDto);
}
