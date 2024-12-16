package com.nhnacademy.book.member.domain.controller;

import com.nhnacademy.book.member.domain.dto.auth.AuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;
import com.nhnacademy.book.member.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 권한 생성
    @PostMapping("/auths")
    public ResponseEntity<AuthResponseDto> createAuth(@RequestBody AuthRequestDto requestDto) {
        AuthResponseDto createdAuth = authService.createAuth(requestDto.getName());
        return new ResponseEntity<>(createdAuth, HttpStatus.CREATED);
    }

    // 모든 권한 조회
    @GetMapping("/auths")
    public ResponseEntity<List<AuthResponseDto>> getAllAuths() {
        List<AuthResponseDto> auths = authService.getAllAuths();
        return new ResponseEntity<>(auths, HttpStatus.OK);
    }

    // 권한 ID로 조회
    @GetMapping("/auths/{authId}")
    public ResponseEntity<AuthResponseDto> getAuthById(@PathVariable Long authId) {
        AuthResponseDto auth = authService.getAuthById(authId)
                .orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다."));
        return new ResponseEntity<>(auth, HttpStatus.OK);
    }

    // 권한 수정
    @PutMapping("/auths/{authId}")
    public ResponseEntity<AuthResponseDto> updateAuth(@PathVariable Long authId,
                                                      @RequestBody AuthRequestDto requestDto) {
        AuthResponseDto updatedAuth = authService.updateAuth(authId, requestDto.getName());
        return new ResponseEntity<>(updatedAuth, HttpStatus.OK);
    }

    // 권한 삭제
    @DeleteMapping("/auths/{authId}")
    public ResponseEntity<Void> deleteAuth(@PathVariable Long authId) {
        authService.deleteAuth(authId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

