package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public interface AuthService {
    List<AuthResponseDto> getAllAuths();
}

