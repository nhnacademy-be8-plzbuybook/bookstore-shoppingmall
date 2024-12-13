package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    public AuthResponseDto getAuthById(Long authId) {
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new IllegalArgumentException("권한을 찾을 수 없습니다."));
        return new AuthResponseDto(auth.getAuthId(), auth.getAuthName());
    }



}
