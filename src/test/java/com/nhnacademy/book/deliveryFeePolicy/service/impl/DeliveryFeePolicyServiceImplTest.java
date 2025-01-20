package com.nhnacademy.book.deliveryFeePolicy.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicySaveRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyUpdateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.repository.DeliveryFeePolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryFeePolicyServiceImplTest {
    @Mock
    private DeliveryFeePolicyRepository deliveryFeePolicyRepository;
    @InjectMocks
    private DeliveryFeePolicyServiceImpl deliveryFeePolicyService;

    @DisplayName("배송비정책 목록조회: 성공")
    @Test
    void getDeliveryFeePolicies_success() {
        List<DeliveryFeePolicy> policies = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            policies.add(new DeliveryFeePolicy((long) i, "policyName" + i, BigDecimal.valueOf(3000), BigDecimal.valueOf(3000)));
        }
        when(deliveryFeePolicyRepository.findAll()).thenReturn(policies);

        //when
        List<DeliveryFeePolicyDto> policyDtos = deliveryFeePolicyService.getDeliveryPolicies();

        //then
        assertNotNull(policyDtos);
        assertEquals(5, policyDtos.size());
    }

    @DisplayName("배송비정책 조회: 성공")
    @Test
    void getDeliveryFeePolicy_success() {
        //given
        long id = 1L;
        String name = "기본 배송비정책";
        BigDecimal defaultFee = new BigDecimal("3000");
        BigDecimal freeThreshold = new BigDecimal("30000");
        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(1L, name, defaultFee, freeThreshold);

        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));

        //when
        DeliveryFeePolicyDto foundPolicy = deliveryFeePolicyService.getDeliveryFeePolicy(id);

        //then
        verify(deliveryFeePolicyRepository).findById(id);
        assertEquals(name, foundPolicy.name());
        assertEquals(defaultFee, foundPolicy.defaultDeliveryFee());
        assertEquals(freeThreshold, foundPolicy.freeDeliveryThreshold());
    }

    @DisplayName("배송비정책 조회: 배송비정책 못찾음")
    @Test
    void getDeliveryFeePolicy_not_found() {
        //given
        long id = 1L;

        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.empty());

        //when
        NotFoundException e = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.getDeliveryFeePolicy(id));

        //then
        verify(deliveryFeePolicyRepository).findById(id);
        assertEquals("찾을 수 없는 배송비정책입니다. 배송비정책아이디: " + id, e.getMessage());
    }


    @DisplayName("배송비정책 생성: 성공")
    @Test
    void createDeliveryFeePolicy_success() {
        // Given
        String name = "배송비정책";
        BigDecimal defaultFee = new BigDecimal("3000");
        BigDecimal freeThreshold = new BigDecimal("30000");
        DeliveryFeePolicySaveRequestDto saveRequest = new DeliveryFeePolicySaveRequestDto(name, defaultFee, freeThreshold);
        DeliveryFeePolicy savedPolicy = new DeliveryFeePolicy(1L, name, defaultFee, freeThreshold);

        when(deliveryFeePolicyRepository.existsByName(name)).thenReturn(false);
        when(deliveryFeePolicyRepository.save(any(DeliveryFeePolicy.class))).thenReturn(savedPolicy);

        // When
        long actualId = deliveryFeePolicyService.createDeliveryFeePolicy(saveRequest);

        // Then
        assertEquals(1L, actualId);
        verify(deliveryFeePolicyRepository).save(any(DeliveryFeePolicy.class));
    }

    @DisplayName("배송비정책 생성: 중복")
    @Test
    void createDeliveryFeePolicy_duplicated() {
        //given
        String name = "기본 배송비정책";
        BigDecimal defaultFee = new BigDecimal("3000");
        BigDecimal freeThreshold = new BigDecimal("30000");
        DeliveryFeePolicySaveRequestDto saveRequest = new DeliveryFeePolicySaveRequestDto(name, defaultFee, freeThreshold);

        when(deliveryFeePolicyRepository.existsByName(name)).thenReturn(true);

        //when
        ConflictException e = assertThrows(ConflictException.class, () -> deliveryFeePolicyService.createDeliveryFeePolicy(saveRequest));

        //then
        verify(deliveryFeePolicyRepository).existsByName(name);
        verify(deliveryFeePolicyRepository, never()).save(any(DeliveryFeePolicy.class));
        assertEquals("이미 존재하는 배송비정책입니다.", e.getMessage());
    }

    @DisplayName("배송비정책 수정: 성공")
    @Test
    void modifyDeliveryFeePolicy_success() {
        // Given
        long id = 1L;
        String name = "배송비정책";
        BigDecimal newDefaultFee = new BigDecimal("4000");
        BigDecimal newFreeThreshold = new BigDecimal("40000");
        DeliveryFeePolicyUpdateRequestDto updateRequest = new DeliveryFeePolicyUpdateRequestDto(name, newDefaultFee, newFreeThreshold);
        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, "name", new BigDecimal("3000"), new BigDecimal("30000"));

        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));

        // When
        long actualId = deliveryFeePolicyService.modifyDeliveryFeePolicy(id, updateRequest);

        // Then
        assertEquals(id, actualId);
        assertEquals(newDefaultFee, existingPolicy.getDefaultDeliveryFee());
        assertEquals(newFreeThreshold, existingPolicy.getFreeDeliveryThreshold());
        verify(deliveryFeePolicyRepository).findById(id);
    }

    @DisplayName("배송비정책 수정: 배송비정책 못찾음")
    @Test
    void modifyDeliveryFeePolicy_not_found() {
        //given
        long id = 1000000000L;
        String updatedName = "updatedPolicy";
        BigDecimal defaultFee = new BigDecimal("3000");
        BigDecimal freeThreshold = new BigDecimal("30000");
        DeliveryFeePolicyUpdateRequestDto updateRequest = new DeliveryFeePolicyUpdateRequestDto(updatedName, defaultFee, freeThreshold);
        DeliveryFeePolicy policy = mock(DeliveryFeePolicy.class);
        Optional<DeliveryFeePolicy> optionalPolicy = Optional.empty();

        when(deliveryFeePolicyRepository.findById(id)).thenReturn(optionalPolicy);

        //when
        NotFoundException e = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.modifyDeliveryFeePolicy(id, updateRequest));

        //then
        assertEquals(id + " policy not found!", e.getMessage());
        verify(deliveryFeePolicyRepository).findById(id);
        verify(policy, never()).update(updateRequest.name(), updateRequest.defaultDeliveryFee(), updateRequest.freeDeliveryThreshold());
        verify(policy, never()).getId();
    }


    @DisplayName("배송비정책 삭제: 성공")
    @Test
    void removeDeliveryFeePolicy_success() {
        //given
        long id = 1L;

        when(deliveryFeePolicyRepository.existsById(id)).thenReturn(true);
        doNothing().when(deliveryFeePolicyRepository).deleteById(id);

        //when
        deliveryFeePolicyService.removeDeliveryFeePolicy(id);

        //then
        verify(deliveryFeePolicyRepository).existsById(id);
        verify(deliveryFeePolicyRepository).deleteById(id);
    }

    @DisplayName("배송비정책 삭제: 배송비정책 없음")
    @Test
    void removeDeliveryFeePolicy_not_found() {
        // Given
        long id = 100000L;

        when(deliveryFeePolicyRepository.existsById(id)).thenReturn(false);

        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.removeDeliveryFeePolicy(id));

        // Then
        assertEquals("찾을 수 없는 배송비정책입니다. 배송비정책아이디: " + id, exception.getMessage());
        verify(deliveryFeePolicyRepository).existsById(id);
        verify(deliveryFeePolicyRepository, never()).delete(any());
    }

    @DisplayName("배송비 계산: 무료 배송")
    @Test
    void getCalculatedDeliveryFee_success_free() {
        //given
        long id = 1L;
        BigDecimal price = new BigDecimal("35000");
        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(price);
        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, "policyName", new BigDecimal("3000"), new BigDecimal("30000"));

        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));

        //when
        BigDecimal calculatedFee = deliveryFeePolicyService.getCalculatedDeliveryFee(id, calculateRequest);

        //then
        assertEquals(BigDecimal.ZERO, calculatedFee);
        verify(deliveryFeePolicyRepository).findById(id);
    }

    @DisplayName("배송비 계산: 기본 배송비정책")
    @Test
    void getCalculatedDeliveryFee_success_default_fee() {
//given
        long id = 1L;
        BigDecimal price = new BigDecimal("25000");
        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(price);
        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, "policyName", new BigDecimal("3000"), new BigDecimal("30000"));

        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));

        //when
        BigDecimal calculatedFee = deliveryFeePolicyService.getCalculatedDeliveryFee(id, calculateRequest);

        //then
        assertEquals(existingPolicy.getDefaultDeliveryFee(), calculatedFee);
        verify(deliveryFeePolicyRepository).findById(id);
    }

    @DisplayName("배송비정책 계산: 배송비정책 못찾음")
    @Test
    void getCalculatedDeliveryFee_not_found() {
        // Given
        long id = 100000L;
        BigDecimal price = new BigDecimal("3000");
        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(price);

        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.empty());

        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.getCalculatedDeliveryFee(id, calculateRequest));

        // Then
        assertEquals("찾을 수 없는 배송비정책입니다. 배송비정책아이디: " + id, exception.getMessage());
        verify(deliveryFeePolicyRepository).findById(id);
        verify(deliveryFeePolicyRepository, never()).delete(any());
    }
}
