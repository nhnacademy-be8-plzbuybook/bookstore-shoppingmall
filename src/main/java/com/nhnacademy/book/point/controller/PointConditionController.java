package com.nhnacademy.book.point.controller;

import com.nhnacademy.book.point.dto.PointConditionRequestDto;
import com.nhnacademy.book.point.dto.PointConditionResponseDto;
import com.nhnacademy.book.point.service.PointConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PointConditionController {

    private final PointConditionService pointConditionService;

    @PostMapping("/points/conditions")
    public ResponseEntity<PointConditionResponseDto> creatPointCondition(@RequestBody PointConditionRequestDto pointConditionRequestDto) {
        try {
            PointConditionResponseDto createdPointCondition = pointConditionService.createPointCondition(pointConditionRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPointCondition);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/points/conditions")
    public ResponseEntity<List<PointConditionResponseDto>> getAllPointConditions() {
        List<PointConditionResponseDto> pointConditions = pointConditionService.getAllPointConditions();
        return ResponseEntity.ok(pointConditions);
    }

    @PostMapping("/points/conditions/{id}")
    public ResponseEntity<PointConditionResponseDto> updatePointCondition(@PathVariable Long id, @RequestBody PointConditionRequestDto pointConditionRequestDto) {
        PointConditionResponseDto updatedPointCondition = pointConditionService.updatePointCondition(id, pointConditionRequestDto);
        return ResponseEntity.ok(updatedPointCondition);
    }

}