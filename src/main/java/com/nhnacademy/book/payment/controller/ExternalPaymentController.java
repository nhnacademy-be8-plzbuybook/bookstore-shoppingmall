package com.nhnacademy.book.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/payments")
@RestController
public class ExternalPaymentController {
    private final PaymentService paymentService;


    @PostMapping("/confirm/widget")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody PaymentConfirmRequestDto confirmRequest) throws JsonProcessingException {
        // 결제 요청 전 저장한 정보와 같은지 검증
        paymentService.verifyPayment(confirmRequest);
        JSONObject response = paymentService.confirmPayment(confirmRequest);


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}