package com.nhnacademy.book.point.service.Impl;


import com.nhnacademy.book.member.domain.exception.PointConditionNotFoundException;
import com.nhnacademy.book.point.domain.PointCondition;
import com.nhnacademy.book.point.dto.PointConditionRequestDto;
import com.nhnacademy.book.point.dto.PointConditionResponseDto;
import com.nhnacademy.book.point.repository.PointConditionRepository;
import com.nhnacademy.book.point.service.PointConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.nhnacademy.book.point.service.Impl.MemberPointServiceImpl.POINT_CONDITION_NOT_FOUND;

@Transactional
@Service
@RequiredArgsConstructor
public class PointConditionServiceImpl implements PointConditionService {

    private final PointConditionRepository pointConditionRepository;

    @Override
    public PointConditionResponseDto createPointCondition(PointConditionRequestDto pointConditionRequestDto) {

        if (pointConditionRequestDto.getConditionPoint() != null && pointConditionRequestDto.getConditionPercentage() != null) {
            throw new IllegalStateException("포인트와 비율 중 하나만 입력할 수 있습니다.");
        }

        if (pointConditionRequestDto.getConditionPoint() == null && pointConditionRequestDto.getConditionPercentage() == null) {
            throw new IllegalArgumentException("포인트와 비율 중 하나를 입력해야 합니다.");
        }

        PointCondition pointCondition = new PointCondition();
        pointCondition.setName(pointConditionRequestDto.getName());
        pointCondition.setConditionPoint(pointConditionRequestDto.getConditionPoint());
        pointCondition.setConditionPercentage(pointConditionRequestDto.getConditionPercentage());
        pointCondition.setStatus(true);

        PointCondition savedPointCondition = pointConditionRepository.save(pointCondition);

        return new PointConditionResponseDto(
                savedPointCondition.getPointConditionId(),
                savedPointCondition.getName(),
                savedPointCondition.getConditionPoint(),
                savedPointCondition.getConditionPercentage(),
                savedPointCondition.isStatus()

        );
    }

    @Override
    public List<PointConditionResponseDto> getAllPointConditions() {
        return pointConditionRepository.findAll().stream()
                .map(pointCondition -> new PointConditionResponseDto(
                        pointCondition.getPointConditionId(),
                        pointCondition.getName(),
                        pointCondition.getConditionPoint(),
                        pointCondition.getConditionPercentage(),
                        pointCondition.isStatus()
                ))
                .toList();
    }


    @Override
    public PointConditionResponseDto updatePointCondition(Long id, PointConditionRequestDto pointConditionRequestDto) {
        PointCondition existingPointCondition = pointConditionRepository.findById(id).orElseThrow(() -> new PointConditionNotFoundException(POINT_CONDITION_NOT_FOUND));

        existingPointCondition.setName(pointConditionRequestDto.getName());
        existingPointCondition.setConditionPoint(pointConditionRequestDto.getConditionPoint());
        existingPointCondition.setConditionPercentage(pointConditionRequestDto.getConditionPercentage());
        existingPointCondition.setStatus(pointConditionRequestDto.isStatus());

        PointCondition updatedPointCondition = pointConditionRepository.save(existingPointCondition);

        return new PointConditionResponseDto(
                updatedPointCondition.getPointConditionId(),
                updatedPointCondition.getName(),
                updatedPointCondition.getConditionPoint(),
                updatedPointCondition.getConditionPercentage(),
                updatedPointCondition.isStatus()
        );
    }

}
