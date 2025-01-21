package com.nhnacademy.book.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.point.dto.PointConditionRequestDto;
import com.nhnacademy.book.point.dto.PointConditionResponseDto;
import com.nhnacademy.book.point.service.PointConditionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class PointConditionControllerTest {

    @Mock
    private PointConditionService pointConditionService;

    @InjectMocks
    private PointConditionController pointConditionController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders.standaloneSetup(pointConditionController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    @DisplayName("포인트 조건 생성 성공")
    void createPointCondition_Success() throws Exception {
        PointConditionRequestDto requestDto = new PointConditionRequestDto("NEW_CONDITION", 1000, null, true);
        PointConditionResponseDto responseDto = new PointConditionResponseDto(1L, "NEW_CONDITION", 1000, null, true);

        when(pointConditionService.createPointCondition(any(PointConditionRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/points/conditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("NEW_CONDITION"))
                .andExpect(jsonPath("$.conditionPoint").value(1000))
                .andExpect(jsonPath("$.conditionPercentage").doesNotExist())
                .andExpect(jsonPath("$.status").value(true));


    }


    @Test
    @DisplayName("포인트 조건 목록 조회 성공")
    void getAllPointConditions_Success() throws Exception {
        List<PointConditionResponseDto> responseDto = List.of(
                new PointConditionResponseDto(1L, "CONDITION_1", 200, null, true),
                new PointConditionResponseDto(2L, "CONDITION_2", 500, null, false)
        );

        when(pointConditionService.getAllPointConditions()).thenReturn(responseDto);

        mockMvc.perform(get("/api/points/conditions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("CONDITION_1"))
                .andExpect(jsonPath("$[0].conditionPoint").value(200))
                .andExpect(jsonPath("$[1].name").value("CONDITION_2"))
                .andExpect(jsonPath("$[1].conditionPoint").value(500));
    }

    @Test
    @DisplayName("포인트 조건 수정 성공")
    void updatePointCondition_Success() throws Exception {
        Long conditionId = 1L;
        PointConditionRequestDto requestDto = new PointConditionRequestDto("UPDATE_CONDITION", 1000, null, true);
        PointConditionResponseDto responseDto = new PointConditionResponseDto(1L, "UPDATE_CONDITION", 800, null, true);

        when(pointConditionService.updatePointCondition(eq(conditionId), any(PointConditionRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/points/conditions/{id}", conditionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UPDATE_CONDITION"))
                .andExpect(jsonPath("$.conditionPoint").value(800))
                .andExpect(jsonPath("$.conditionPercentage").doesNotExist())
                .andExpect(jsonPath("$.status").value(true));
    }


}


