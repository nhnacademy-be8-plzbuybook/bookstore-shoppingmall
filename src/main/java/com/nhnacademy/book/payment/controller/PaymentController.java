//package com.nhnacademy.book.payment.controller;
//
//import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
//import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
//import com.nhnacademy.book.payment.entity.Payment;
//import com.nhnacademy.book.payment.service.PaymentService;
//import com.nhnacademy.book.payment.service.TossPaymentService;
//import lombok.RequiredArgsConstructor;
//import org.json.simple.JSONObject;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class PaymentController {
//    private final PaymentService paymentService;
//    private final TossPaymentService tossPaymentService;
//
//    @PostMapping("/api/orders/{orderId}/payments/cancel")
//    public ResponseEntity<?> cancelPayment(@PathVariable String orderId, @RequestBody PaymentCancelRequestDto cancelRequest) {
////        cancelRequest.setOrderId(orderId);
//
//        try {
//            Payment payment = paymentService.getByOrderId(orderId);
//            String paymentKey = payment.getPaymentKey();
//
//            JSONObject response = tossPaymentService.cancelPayment(paymentKey, cancelRequest);
//
//            int statusCode = response.containsKey("error") ? 400 : 200;
//
//            if (statusCode == 200) {
//                // paymentMessageService.sendCancelMessage(cancelRequest);
//            }
//            return ResponseEntity.status(statusCode).body(response);
//
//        } catch (NotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
//}
