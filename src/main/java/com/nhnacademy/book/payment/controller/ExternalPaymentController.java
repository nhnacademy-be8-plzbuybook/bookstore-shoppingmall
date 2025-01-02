package com.nhnacademy.book.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.PaymentSaveRequestDto;
import com.nhnacademy.book.payment.dto.SaveAmountDto;
import com.nhnacademy.book.payment.service.PaymentService;
import com.nhnacademy.book.payment.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
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
    private final TossPaymentService tossPaymentService;
    private final ObjectMapper objectMapper;

    @PostMapping("/confirm/widget")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody PaymentConfirmRequestDto confirmRequest) throws JsonProcessingException {
        // 결제 요청 전 저장한 정보와 같은지 검증
        paymentService.verifyPayment(confirmRequest);

        JSONObject response = tossPaymentService.confirmPayment(confirmRequest);
        int statusCode = response.containsKey("error") ? 400 : 200;
        //결제 성공 시 DB에 저장하기 위해 메세지 전송
        if (statusCode == 200) {
//                paymentMessageService.sendConfirmMessage(response);
            paymentService.recordPayment(responseToDto(response));
        }

        return ResponseEntity.status(statusCode).body(response);
    }

    private PaymentSaveRequestDto responseToDto(JSONObject response) throws JsonProcessingException {
        String jsonString = response.toString();
        return objectMapper.readValue(jsonString, PaymentSaveRequestDto.class);
    }
}