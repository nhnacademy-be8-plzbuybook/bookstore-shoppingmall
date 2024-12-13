package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;
import com.nhnacademy.book.member.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 전체 권한 목록 조회
    @GetMapping("/auths")
    public ResponseEntity<List<AuthResponseDto>> getAllAuths() {
        List<AuthResponseDto> responseDtoList = authService.getAllAuths();
        return ResponseEntity.ok(responseDtoList);
    }
}

