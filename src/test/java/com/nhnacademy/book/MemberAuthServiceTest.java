package com.nhnacademy.book;


import com.nhnacademy.book.member.domain.*;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.exception.AuthNotFoundException;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.Impl.MemberAuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@SpringBootTest
public class MemberAuthServiceTest {

    @Autowired
    private MemberAuthServiceImpl memberAuthService;

    @MockBean
    private MemberAuthRepository memberAuthRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AuthRepository authRepository;

    private Member member;
    private Auth auth;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setMemberId(1L);
        member.setName("John");

        auth = new Auth();
        auth.setAuthId(1L);
        auth.setAuthName("ADMIN");
    }

    @Test
    void assignAuth_Success() {
        MemberAuthRequestDto requestDto = new MemberAuthRequestDto(1L, 1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(authRepository.findById(1L)).thenReturn(Optional.of(auth));
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(member);
        memberAuth.setAuth(auth);

        when(memberAuthRepository.save(any(MemberAuth.class))).thenReturn(memberAuth);
        MemberAuthResponseDto responseDto = memberAuthService.assignAuthToMember(requestDto);
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getMemberId());
        assertEquals(1L, responseDto.getAuthId());

        verify(memberRepository).findById(1L);
        verify(authRepository).findById(1L);
        verify(memberAuthRepository).save(any(MemberAuth.class));
    }

    @Test
    void assignAuth_MemberNotFound() {
        MemberAuthRequestDto requestDto = new MemberAuthRequestDto(1L, 1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberAuthService.assignAuthToMember(requestDto));
    }

    @Test
    void assignAuth_AuthNotFound() {
        MemberAuthRequestDto requestDto = new MemberAuthRequestDto(1L, 1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(authRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuthNotFoundException.class, () -> memberAuthService.assignAuthToMember(requestDto));
    }
}
