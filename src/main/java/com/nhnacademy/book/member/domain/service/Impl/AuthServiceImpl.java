package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;

    @Override
    public List<AuthResponseDto> getAllAuths() {
        List<Auth> authList = authRepository.findAll();
        return authList.stream()
                .map(auth -> new AuthResponseDto(auth.getAuthId(), auth.getAuthName()))
                .collect(Collectors.toList());
    }
}
