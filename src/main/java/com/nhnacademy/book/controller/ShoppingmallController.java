package com.nhnacademy.book.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingmallController {

    private final AuthClient authClient;

    public ShoppingmallController(AuthClient authClient) {
        this.authClient = authClient;
    }


    @GetMapping("/hello")
    public String helloFromAuth() {
        return authClient.hello();  // 인증 서버의 /hello 호출
    }
}