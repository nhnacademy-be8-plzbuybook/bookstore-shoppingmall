//package com.nhnacademy.book.deliveryFeePolicy.service.impl;
//
//import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
//import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicySaveRequestDto;
//import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeePolicyUpdateRequestDto;
//import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
//import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
//import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
//import com.nhnacademy.book.deliveryFeePolicy.repository.DeliveryFeePolicyRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class DeliveryFeePolicyServiceImplTest {
//    @Mock
//    private DeliveryFeePolicyRepository deliveryFeePolicyRepository;
//    @InjectMocks
//    private DeliveryFeePolicyServiceImpl deliveryFeePolicyService;
//
//    @Test
//    void getDeliveryFeePolicy_success() {
//        //given
//        long id = 1L;
//        BigDecimal defaultFee = new BigDecimal("3000");
//        BigDecimal freeThreshold = new BigDecimal("30000");
//        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(1L, defaultFee, freeThreshold);
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));
//
//        //when
//        DeliveryFeePolicy foundPolicy = deliveryFeePolicyService.getDeliveryFeePolicy(id);
//
//        //then
//        verify(deliveryFeePolicyRepository).findById(id);
//        assertEquals(defaultFee, foundPolicy.getDefaultDeliveryFee());
//        assertEquals(freeThreshold, foundPolicy.getFreeDeliveryThreshold());
//    }
//
//    @Test
//    void getDeliveryFeePolicy_not_found() {
//        //given
//        long id = 1L;
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.empty());
//
//        //when
//        NotFoundException e = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.getDeliveryFeePolicy(id));
//
//        //then
//        verify(deliveryFeePolicyRepository).findById(id);
//        assertEquals(id + " policy not found!", e.getMessage());
//    }
//
//
//    @Test
//    void createDeliveryFeePolicy_success() {
//        // Given
//        BigDecimal defaultFee = new BigDecimal("3000");
//        BigDecimal freeThreshold = new BigDecimal("30000");
//        DeliveryFeePolicySaveRequestDto saveRequest = new DeliveryFeePolicySaveRequestDto(defaultFee, freeThreshold);
//        DeliveryFeePolicy savedPolicy = new DeliveryFeePolicy(1L, defaultFee, freeThreshold);
//
//        when(deliveryFeePolicyRepository.existsByDefaultDeliveryFeeAndFreeDeliveryThreshold(defaultFee, freeThreshold)).thenReturn(false);
//        when(deliveryFeePolicyRepository.save(any(DeliveryFeePolicy.class))).thenReturn(savedPolicy);
//
//        // When
//        long actualId = deliveryFeePolicyService.createDeliveryFeePolicy(saveRequest);
//
//        // Then
//        assertEquals(1L, actualId);
//        verify(deliveryFeePolicyRepository).existsByDefaultDeliveryFeeAndFreeDeliveryThreshold(defaultFee, freeThreshold);
//        verify(deliveryFeePolicyRepository).save(any(DeliveryFeePolicy.class));
//    }
//
//    @Test
//    void createDeliveryFeePolicy_duplicated() {
//        //given
//        BigDecimal defaultFee = new BigDecimal("3000");
//        BigDecimal freeThreshold = new BigDecimal("30000");
//        DeliveryFeePolicySaveRequestDto saveRequest = new DeliveryFeePolicySaveRequestDto(defaultFee, freeThreshold);
//        DeliveryFeePolicy mockPolicy = mock(DeliveryFeePolicy.class);
//
//        when(deliveryFeePolicyRepository.existsByDefaultDeliveryFeeAndFreeDeliveryThreshold(defaultFee, freeThreshold)).thenReturn(true);
//
//        //when
//        ConflictException e = assertThrows(ConflictException.class, () -> deliveryFeePolicyService.createDeliveryFeePolicy(saveRequest));
//
//        //then
//        verify(deliveryFeePolicyRepository).existsByDefaultDeliveryFeeAndFreeDeliveryThreshold(defaultFee, freeThreshold);
//        verify(deliveryFeePolicyRepository, never()).save(any(DeliveryFeePolicy.class));
//        assertEquals("duplicated delivery fee policy", e.getMessage());
//    }
//
//    @Test
//    void modifyDeliveryFeePolicy_success() {
//        // Given
//        long id = 1L;
//        BigDecimal newDefaultFee = new BigDecimal("4000");
//        BigDecimal newFreeThreshold = new BigDecimal("40000");
//        DeliveryFeePolicyUpdateRequestDto updateRequest = new DeliveryFeePolicyUpdateRequestDto(newDefaultFee, newFreeThreshold);
//        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, new BigDecimal("3000"), new BigDecimal("30000"));
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));
//
//        // When
//        long actualId = deliveryFeePolicyService.modifyDeliveryFeePolicy(id, updateRequest);
//
//        // Then
//        assertEquals(id, actualId);
//        assertEquals(newDefaultFee, existingPolicy.getDefaultDeliveryFee());
//        assertEquals(newFreeThreshold, existingPolicy.getFreeDeliveryThreshold());
//        verify(deliveryFeePolicyRepository).findById(id);
//    }
//
//    @Test
//    void modifyDeliveryFeePolicy_not_found() {
//        //given
//        long id = 1000000000L;
//        BigDecimal defaultFee = new BigDecimal("3000");
//        BigDecimal freeThreshold = new BigDecimal("30000");
//        DeliveryFeePolicyUpdateRequestDto updateRequest = new DeliveryFeePolicyUpdateRequestDto(defaultFee, freeThreshold);
//        DeliveryFeePolicy policy = mock(DeliveryFeePolicy.class);
//        Optional<DeliveryFeePolicy> optionalPolicy = Optional.empty();
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(optionalPolicy);
//
//        //when
//        NotFoundException e = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.modifyDeliveryFeePolicy(id, updateRequest));
//
//        //then
//        assertEquals(id + " policy not found!", e.getMessage());
//        verify(deliveryFeePolicyRepository).findById(id);
//        verify(policy, never()).update(updateRequest.defaultDeliveryFee(), updateRequest.freeDeliveryThreshold());
//        verify(policy, never()).getId();
//    }
//
//
//    @Test
//    void removeDeliveryFeePolicy_success() {
//        //given
//        long id = 1L;
//        DeliveryFeePolicy policy = mock(DeliveryFeePolicy.class);
//        Optional<DeliveryFeePolicy> optionalPolicy = Optional.of(policy);
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(optionalPolicy);
//        doNothing().when(deliveryFeePolicyRepository).delete(policy);
//        //when
//        deliveryFeePolicyService.removeDeliveryFeePolicy(id);
//
//        //then
//        verify(deliveryFeePolicyRepository).findById(id);
//        verify(deliveryFeePolicyRepository).delete(any());
//    }
//
//    @Test
//    void removeDeliveryFeePolicy_not_found() {
//        // Given
//        long id = 100000L;
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.empty());
//
//        // When
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.removeDeliveryFeePolicy(id));
//
//        // Then
//        assertEquals(id + " policy not found!", exception.getMessage());
//        verify(deliveryFeePolicyRepository).findById(id);
//        verify(deliveryFeePolicyRepository, never()).delete(any());
//    }
//
//    @Test
//    void getCalculatedDeliveryFee_success_free() {
//        //given
//        long id = 1L;
//        BigDecimal price = new BigDecimal("35000");
//        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(price);
//        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, new BigDecimal("3000"), new BigDecimal("30000"));
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));
//
//        //when
//        BigDecimal calculatedFee = deliveryFeePolicyService.getCalculatedDeliveryFee(id, calculateRequest);
//
//        //then
//        assertEquals(BigDecimal.ZERO, calculatedFee);
//        verify(deliveryFeePolicyRepository).findById(id);
//    }
//
//    @Test
//    void getCalculatedDeliveryFee_success_default_fee() {
////given
//        long id = 1L;
//        BigDecimal price = new BigDecimal("25000");
//        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(price);
//        DeliveryFeePolicy existingPolicy = new DeliveryFeePolicy(id, new BigDecimal("3000"), new BigDecimal("30000"));
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.of(existingPolicy));
//
//        //when
//        BigDecimal calculatedFee = deliveryFeePolicyService.getCalculatedDeliveryFee(id, calculateRequest);
//
//        //then
//        assertEquals(existingPolicy.getDefaultDeliveryFee(), calculatedFee);
//        verify(deliveryFeePolicyRepository).findById(id);
//    }
//
//    @Test
//    void getCalculatedDeliveryFee_not_found() {
//        // Given
//        long id = 100000L;
//        BigDecimal price = new BigDecimal("3000");
//        DeliveryFeeCalculateRequestDto calculateRequest = new DeliveryFeeCalculateRequestDto(price);
//
//        when(deliveryFeePolicyRepository.findById(id)).thenReturn(Optional.empty());
//
//        // When
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> deliveryFeePolicyService.getCalculatedDeliveryFee(id, calculateRequest));
//
//        // Then
//        assertEquals(id + " policy not found!", exception.getMessage());
//        verify(deliveryFeePolicyRepository).findById(id);
//        verify(deliveryFeePolicyRepository, never()).delete(any());
//    }
//}
