package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auths")
    public ResponseEntity<AuthResponseDto> getAuthById(@RequestParam Long authId) {
        AuthResponseDto responseDto = authService.getAuthById(authId);
        return ResponseEntity.ok(responseDto);
    }




}
