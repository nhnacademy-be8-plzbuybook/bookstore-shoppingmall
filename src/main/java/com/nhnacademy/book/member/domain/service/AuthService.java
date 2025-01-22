package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;

import java.util.List;
import java.util.Optional;

public interface AuthService {
    AuthResponseDto createAuth(String authName);
    List<AuthResponseDto> getAllAuths();
    Optional<AuthResponseDto> getAuthById(Long authId);
    AuthResponseDto updateAuth(Long authId, String newAuthName);
    void deleteAuth(Long authId);
}
