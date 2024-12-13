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

    // 권한 생성
    public Auth createAuth(String authName) {
        Auth auth = new Auth();
        auth.setAuthName(authName);
        return authRepository.save(auth);
    }

}
