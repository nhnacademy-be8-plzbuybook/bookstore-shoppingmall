package com.nhnacademy.book.deliveryFeePolicy.controller;

import com.nhnacademy.book.deliveryFeePolicy.dto.*;
import com.nhnacademy.book.deliveryFeePolicy.entity.DeliveryFeePolicy;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RequiredArgsConstructor
@RequestMapping("/api/delivery-fee-policies")
@RestController
public class DeliveryFeePolicyController {
    private final DeliveryFeePolicyService deliveryFeePolicyService;

    // 주문금액을 기반으로 배송비를 계산
    @PostMapping("/{policy-id}/calculated")
    public ResponseEntity<DeliveryFeeCalculateResponseDto> calculateFee(@PathVariable("policy-id") long id,
                                          @RequestBody DeliveryFeeCalculateRequestDto calculateRequest) {
        BigDecimal calculatedFee = deliveryFeePolicyService.getCalculatedDeliveryFee(id, calculateRequest);
        DeliveryFeeCalculateResponseDto body = new DeliveryFeeCalculateResponseDto(calculatedFee);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/general")
    public ResponseEntity<DeliveryFeePolicy> getGeneralPolicy() {
        DeliveryFeePolicy body = deliveryFeePolicyService.getDeliveryFeePolicy(1L);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/{policy-id}")
    public ResponseEntity<?> getPolicy(@PathVariable("policy-id") long id) {
        DeliveryFeePolicy body = deliveryFeePolicyService.getDeliveryFeePolicy(id);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping
    public ResponseEntity<?> createPolicy(@Valid @RequestBody DeliveryFeePolicySaveRequestDto saveRequest) {
        long id = deliveryFeePolicyService.createDeliveryFeePolicy(saveRequest);
        DeliveryFeePolicySaveResponseDto body = new DeliveryFeePolicySaveResponseDto(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{policy-id}")
    public ResponseEntity<DeliveryFeePolicyUpdateResponseDto> modifyPolicy(@PathVariable("policy-id") long id,
                                          @Valid @RequestBody DeliveryFeePolicyUpdateRequestDto updateRequest) {
        id = deliveryFeePolicyService.modifyDeliveryFeePolicy(id, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new DeliveryFeePolicyUpdateResponseDto(id));
    }

    @DeleteMapping("/{policy-id}")
    public ResponseEntity<Void> removePolicy(@PathVariable("policy-id") long id) {
        deliveryFeePolicyService.removeDeliveryFeePolicy(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
