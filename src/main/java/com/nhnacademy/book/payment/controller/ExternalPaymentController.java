package com.nhnacademy.book.payment.controller;

import com.nhnacademy.book.payment.dto.PaymentConfirmRequestDto;
import com.nhnacademy.book.payment.dto.SaveAmountDto;
import com.nhnacademy.book.payment.service.TossPaymentService;
import com.nhnacademy.book.payment.service.impl.PaymentMessageService;
import com.nhnacademy.book.payment.service.impl.PaymentServiceImpl;
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
    private final PaymentServiceImpl paymentService;
    private final PaymentMessageService paymentMessageService;
    private final TossPaymentService tossPaymentService;

    @PostMapping("/save-payment")
    public ResponseEntity<?> saveAmount(@RequestBody SaveAmountDto saveAmount) {
        paymentService.saveAmountOnRedis(saveAmount);
        return ResponseEntity.ok("Success to save amount!");
    }

    @PostMapping("/confirm/widget")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody PaymentConfirmRequestDto confirmRequest) {
        // 결제 요청 전 저장한 정보와 같은지 검증
        boolean isValid = paymentService.verifyPayment(confirmRequest);

        if (isValid) {
            JSONObject response = tossPaymentService.confirmPayment(confirmRequest);
            int statusCode = response.containsKey("error") ? 400 : 200;
            //결제 성공 시 DB에 저장하기 위해 메세지 전송
            if (statusCode == 200) {
                paymentMessageService.sendConfirmMessage(response);
            }
            return ResponseEntity.status(statusCode).body(response);
        }
        return ResponseEntity.status(400).body(new JSONObject());
    }
}
