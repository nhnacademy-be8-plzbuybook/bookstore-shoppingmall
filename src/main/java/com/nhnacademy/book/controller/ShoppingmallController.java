package com.nhnacademy.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShoppingmallController {

    private final AuthClient authClient;


    @GetMapping("/hello")
    public String helloFromAuth() {
        return authClient.hello();  // 인증 서버의 /hello 호출
    }
}