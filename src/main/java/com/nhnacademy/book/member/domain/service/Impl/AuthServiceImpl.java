package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;

    @Override
    public AuthResponseDto createAuth(String authName) {
        Auth auth = new Auth();
        auth.setAuthName(authName);
        Auth savedAuth = authRepository.save(auth);
        return new AuthResponseDto(savedAuth.getAuthId(), savedAuth.getAuthName());
    }

    @Override
    public List<AuthResponseDto> getAllAuths() {
        return authRepository.findAll().stream()
                .map(auth -> new AuthResponseDto(auth.getAuthId(), auth.getAuthName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthResponseDto> getAuthById(Long authId) {
        return authRepository.findById(authId)
                .map(auth -> new AuthResponseDto(auth.getAuthId(), auth.getAuthName()));
    }

    @Override
    public AuthResponseDto updateAuth(Long authId, String newAuthName) {
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다"));
        auth.setAuthName(newAuthName);
        Auth updatedAuth = authRepository.save(auth);
        return new AuthResponseDto(updatedAuth.getAuthId(), updatedAuth.getAuthName());
    }

    @Override
    public void deleteAuth(Long authId) {
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다"));
        authRepository.delete(auth);
    }
}
