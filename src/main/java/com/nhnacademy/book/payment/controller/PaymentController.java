package com.nhnacademy.book.payment.controller;

import com.nhnacademy.book.payment.PaymentService;
import com.nhnacademy.book.payment.dto.SaveAmountDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/payments")
@RestController
public class PaymentController {
    private final PaymentService paymentService;
    private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    private static final String TOSS_PAYMENT_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    @PostMapping("/save-payment")
    public ResponseEntity<?> saveAmount(@RequestBody SaveAmountDto saveAmount) {
        paymentService.saveAmountOnRedis(saveAmount);
        return ResponseEntity.ok("Success to save amount!");
    }


    @PostMapping("/confirm/widget")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {
        JSONObject requestData = paymentService.parseRequestData(jsonBody);

        // 결제 요청 전 저장한 정보와 같은지 비교
        boolean isValid = paymentService.verifyPayment(requestData);

        if (isValid) {
            JSONObject response = paymentService.sendRequest(requestData, WIDGET_SECRET_KEY, TOSS_PAYMENT_CONFIRM_URL);

            int statusCode = response.containsKey("error") ? 400 : 200;
            return ResponseEntity.status(statusCode).body(response);
        }
        return ResponseEntity.status(400).body(new JSONObject());
    }
}
