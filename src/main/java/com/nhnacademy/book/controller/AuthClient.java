package com.nhnacademy.book.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "authentication")
public interface AuthClient {

    @GetMapping("/hello")
    String hello();

    //test
}