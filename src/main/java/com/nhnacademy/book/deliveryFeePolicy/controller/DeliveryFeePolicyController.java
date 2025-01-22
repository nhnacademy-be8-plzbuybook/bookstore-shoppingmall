package com.nhnacademy.book.deliveryFeePolicy.controller;

import com.nhnacademy.book.deliveryFeePolicy.dto.*;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


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
    public ResponseEntity<DeliveryFeePolicyDto> getGeneralPolicy() {
        DeliveryFeePolicyDto policy = deliveryFeePolicyService.getDeliveryFeePolicy(1L);
        return ResponseEntity.status(HttpStatus.OK).body(policy);
    }


    /**
     * 베송비정책 목록조회
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<DeliveryFeePolicyDto>> getPolicies() {
        List<DeliveryFeePolicyDto> policies = deliveryFeePolicyService.getDeliveryPolicies();
        return ResponseEntity.status(HttpStatus.OK).body(policies);
    }


    /**
     * 배송비정채 단건조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{policy-id}")
    public ResponseEntity<DeliveryFeePolicyDto> getPolicy(@PathVariable("policy-id") long id) {
        DeliveryFeePolicyDto policy = deliveryFeePolicyService.getDeliveryFeePolicy(id);
        return ResponseEntity.status(HttpStatus.OK).body(policy);
    }


    /**
     * 배송비정책 생성
     *
     * @param saveRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<DeliveryFeePolicySaveResponseDto> createPolicy(@Valid @RequestBody DeliveryFeePolicySaveRequestDto saveRequest) {
        long id = deliveryFeePolicyService.createDeliveryFeePolicy(saveRequest);
        DeliveryFeePolicySaveResponseDto body = new DeliveryFeePolicySaveResponseDto(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    /**
     * 배송비정책 수정
     *
     * @param id
     * @param updateRequest
     * @return
     */
    @PutMapping("/{policy-id}")
    public ResponseEntity<DeliveryFeePolicyUpdateResponseDto> modifyPolicy(@PathVariable("policy-id") long id,
                                          @Valid @RequestBody DeliveryFeePolicyUpdateRequestDto updateRequest) {
        id = deliveryFeePolicyService.modifyDeliveryFeePolicy(id, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new DeliveryFeePolicyUpdateResponseDto(id));
    }


    /**
     * 배송비정책 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{policy-id}")
    public ResponseEntity<Void> removePolicy(@PathVariable("policy-id") long id) {
        deliveryFeePolicyService.removeDeliveryFeePolicy(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
