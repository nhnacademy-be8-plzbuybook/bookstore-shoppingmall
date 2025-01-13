//package com.nhnacademy.book.deliveryFeePolicy.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.book.deliveryFeePolicy.dto.*;
//import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
//import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
//import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
//import com.nhnacademy.book.deliveryFeePolicy.service.impl.DeliveryFeePolicyServiceImpl;
//import com.nhnacademy.book.handler.GlobalExceptionHandler;
//import com.nhnacademy.book.member.domain.dto.ErrorResponseDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(DeliveryFeePolicyController.class)
//class DeliveryFeePolicyControllerTest {
//    private MockMvc mockMvc;
//    @MockBean
//    private DeliveryFeePolicyServiceImpl deliveryFeePolicyService;
//    @Autowired
//    private ObjectMapper objectMapper;
//    private final String BASE_URL = "/api/delivery-fee-policies";
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new DeliveryFeePolicyController(deliveryFeePolicyService))
//                .setControllerAdvice(new GlobalExceptionHandler())
//                .build();
//    }
//
//    @Test
//    void getPolicy_not_found() throws Exception {
//        //given
//        long id = 1L;
//        String url = BASE_URL + String.format("/%s", id);
//
//        when(deliveryFeePolicyService.getDeliveryFeePolicy(id)).thenThrow(new NotFoundException(id + " policy not found!"));
//
//        //when
//        MvcResult result = mockMvc.perform(get(url)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        ErrorResponseDto errorResponse = objectMapper.readValue(response, ErrorResponseDto.class);
//        assertEquals(404, errorResponse.getStatus());
//        assertEquals(id + " policy not found!", errorResponse.getMessage());
//    }
//
//    @Test
//    void getPolicy() throws Exception {
//        //given
//        long id = 1L;
//        String url = BASE_URL + "/" + id;
//        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, new BigDecimal("3000"), new BigDecimal("30000"));
//
//        when(deliveryFeePolicyService.getDeliveryFeePolicy(id)).thenReturn(existingPolicy);
//        String resultContent = objectMapper.writeValueAsString(existingPolicy);
//
//        //when
//        mockMvc.perform(get(url)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(resultContent));
//    }
//
//    @Test
//    void createPolicy() throws Exception {
//        //given
//        BigDecimal defaultFee = new BigDecimal("3000");
//        BigDecimal freeThreshold = new BigDecimal("30000");
//        DeliveryFeePolicySaveRequestDto saveRequest = new DeliveryFeePolicySaveRequestDto(defaultFee, freeThreshold);
//        long id = 1L;
//
//        when(deliveryFeePolicyService.createDeliveryFeePolicy(any(DeliveryFeePolicySaveRequestDto.class))).thenReturn(id);
//        String resultContent = objectMapper.writeValueAsString(new DeliveryFeePolicySaveResponseDto(id));
//
//        //when
//        mockMvc.perform(post(BASE_URL)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(saveRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(content().string(resultContent));
//    }
//
//    @Test
//    void createPolicy_conflict() throws Exception {
//        //given
//        BigDecimal defaultFee = new BigDecimal("3000");
//        BigDecimal freeThreshold = new BigDecimal("30000");
//        DeliveryFeePolicySaveRequestDto saveRequest = new DeliveryFeePolicySaveRequestDto(defaultFee, freeThreshold);
//
//        when(deliveryFeePolicyService.createDeliveryFeePolicy(any(DeliveryFeePolicySaveRequestDto.class))).thenThrow(new ConflictException("duplicated delivery fee policy"));
//
//        //when
//        MvcResult result = mockMvc.perform(post(BASE_URL)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(saveRequest)))
//                .andExpect(status().isConflict())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        ErrorResponseDto errorResponse = objectMapper.readValue(response, ErrorResponseDto.class);
//        assertEquals(409, errorResponse.getStatus());
//        assertEquals("duplicated delivery fee policy", errorResponse.getMessage());
//    }
//
//    @Test
//    void modifyPolicy() throws Exception {
//        //given
//        long id = 1L;
//        String url = BASE_URL + "/" + id;
//        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, new BigDecimal("3000"), new BigDecimal("30000"));
//        DeliveryFeePolicyUpdateRequestDto updateRequest = new DeliveryFeePolicyUpdateRequestDto(new BigDecimal("4000"), new BigDecimal("40000"));
//        when(deliveryFeePolicyService.modifyDeliveryFeePolicy(anyLong(), any(DeliveryFeePolicyUpdateRequestDto.class))).thenReturn(id);
//
//        //when
//        mockMvc.perform(put(url)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(new DeliveryFeePolicyUpdateResponseDto(id))));
//
//
//    }
//
//    @Test
//    void modifyPolicy_not_found() throws Exception {
//        //given
//        long id = 1L;
//        String url = BASE_URL + "/" + id;
//        DeliveryFeePolicyUpdateRequestDto updateRequest = new DeliveryFeePolicyUpdateRequestDto(new BigDecimal("4000"), new BigDecimal("40000"));
//
//        when(deliveryFeePolicyService.modifyDeliveryFeePolicy(anyLong(), any(DeliveryFeePolicyUpdateRequestDto.class))).thenThrow(new NotFoundException(id + " policy not found!"));
//
//        //when
//        MvcResult result = mockMvc.perform(put(url)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(updateRequest)))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        ErrorResponseDto errorResponse = objectMapper.readValue(response, ErrorResponseDto.class);
//        assertEquals(404, errorResponse.getStatus());
//        assertEquals(id + " policy not found!", errorResponse.getMessage());
//    }
//
//    @Test
//    void removePolicy() throws Exception {
//        long id = 1L;
//        String url = BASE_URL + "/" + id;
//
//        doNothing().when(deliveryFeePolicyService).removeDeliveryFeePolicy(id);
//
//        mockMvc.perform(delete(url)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void removePolicy_not_found() throws Exception {
//        long id = 1L;
//        String url = BASE_URL + "/" + id;
//
//        doThrow(new NotFoundException(id + " policy not found!")).when(deliveryFeePolicyService).removeDeliveryFeePolicy(id);
//
//        MvcResult result = mockMvc.perform(delete(url)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        ErrorResponseDto errorResponse = objectMapper.readValue(response, ErrorResponseDto.class);
//        assertEquals(404, errorResponse.getStatus());
//        assertEquals(id + " policy not found!", errorResponse.getMessage());
//    }
//
//    @Test
//    void calculateFee_free() throws Exception {
//        //given
//        long id = 1L;
//        String url = BASE_URL + "/" + id + "/calculated";
//        BigDecimal orderPrice = new BigDecimal("35000");
//        BigDecimal freeFee = BigDecimal.ZERO;
//        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(orderPrice);
//
//        when(deliveryFeePolicyService.getCalculatedDeliveryFee(anyLong(), any(DeliveryFeeCalculateRequestDto.class))).thenReturn(freeFee);
//
//        DeliveryFeeCalculateResponseDto calculateResponse = new DeliveryFeeCalculateResponseDto(freeFee);
//        //when
//        mockMvc.perform(post(url)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(calculateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(calculateResponse)));
//    }
//
//    @Test
//    void calculateFee_default() throws Exception {
//        //given
//        long id = 1L;
//        String url = BASE_URL + "/" + id + "/calculated";
//        BigDecimal orderPrice = new BigDecimal("25000");
//        BigDecimal defaultFee = new BigDecimal("3000");
//        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(orderPrice);
//
//        when(deliveryFeePolicyService.getCalculatedDeliveryFee(anyLong(), any(DeliveryFeeCalculateRequestDto.class))).thenReturn(defaultFee);
//
//        DeliveryFeeCalculateResponseDto calculateResponse = new DeliveryFeeCalculateResponseDto(defaultFee);
//        //when
//        mockMvc.perform(post(url)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(calculateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(calculateResponse)));
//
//    }
//
//    @Test
//    void calculateFee_policy_not_found() throws Exception {
//        //given
//        long id = 100000L;
//        String url = BASE_URL + "/" + id + "/calculated";
//        BigDecimal orderPrice = new BigDecimal("25000");
//        BigDecimal defaultFee = new BigDecimal("3000");
//        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(orderPrice);
//
//        when(deliveryFeePolicyService.getCalculatedDeliveryFee(anyLong(), any(DeliveryFeeCalculateRequestDto.class)))
//                .thenThrow(new NotFoundException(id + " policy not found!"));
//
//        //when
//        MvcResult result = mockMvc.perform(post(url)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(calculateRequest)))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        ErrorResponseDto errorResponse = objectMapper.readValue(response, ErrorResponseDto.class);
//        assertEquals(404, errorResponse.getStatus());
//        assertEquals(id + " policy not found!", errorResponse.getMessage());
//
//    }
//}