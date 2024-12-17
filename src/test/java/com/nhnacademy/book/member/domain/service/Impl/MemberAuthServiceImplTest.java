//package com.nhnacademy.book.member.domain.service.Impl;
//
//
//import com.nhnacademy.book.member.domain.*;
//import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
//import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
//import com.nhnacademy.book.member.domain.exception.AuthNotFoundException;
//import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
//import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
//import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
//import com.nhnacademy.book.member.domain.repository.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//
//public class MemberAuthServiceImplTest {
//
//    @InjectMocks
//    private MemberAuthServiceImpl memberAuthService;
//
//    @Mock
//    private MemberAuthRepository memberAuthRepository;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private AuthRepository authRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void assignAuth_whenValid() {
//        MemberAuthRequestDto requestDto = new MemberAuthRequestDto(1L, 1L);
//        Member member = new Member();
//        member.setMemberId(1L);
//        Auth auth = new Auth();
//        auth.setAuthId(1L);
//
//        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
//        when(authRepository.findById(1L)).thenReturn(Optional.of(auth));
//        when(memberAuthRepository.save(any(MemberAuth.class))).thenReturn(new MemberAuth(member, auth));
//
//        MemberAuthResponseDto responseDto = memberAuthService.assignAuthToMember(requestDto);
//
//        assertEquals(1L, responseDto.getMemberId());
//        assertEquals(1L, responseDto.getAuthId());
//        verify(memberAuthRepository, times(1)).save(any(MemberAuth.class));
//    }
//
////    @Test
////    void assignAuth_Success() {
////        MemberAuthRequestDto requestDto = new MemberAuthRequestDto(1L, 1L);
////
////        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
////        when(authRepository.findById(1L)).thenReturn(Optional.of(auth));
////        MemberAuth memberAuth = new MemberAuth();
////        memberAuth.setMember(member);
////        memberAuth.setAuth(auth);
////
////        when(memberAuthRepository.save(any(MemberAuth.class))).thenReturn(memberAuth);
////        MemberAuthResponseDto responseDto = memberAuthService.assignAuthToMember(requestDto);
////
////        assertNotNull(responseDto);
////        assertEquals(1L, responseDto.getMemberId());
////        assertEquals(1L, responseDto.getAuthId());
////
////        verify(memberRepository).findById(1L);
////        verify(authRepository).findById(1L);
////        verify(memberAuthRepository).save(any(MemberAuth.class));
////    }
////
////    @Test
////        // 멤버가 존재하지 않을 경우
////    void assignAuth_MemberNotFound() {
////        MemberAuthRequestDto requestDto = new MemberAuthRequestDto(1L, 1L);
////
////        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
////
////        assertThrows(MemberNotFoundException.class, () -> memberAuthService.assignAuthToMember(requestDto));
////    }
////
////    @Test
////        // 권한이 존재하지 않을 경우
////    void assignAuth_AuthNotFound() {
////        MemberAuthRequestDto requestDto = new MemberAuthRequestDto(1L, 1L);
////
////        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
////        when(authRepository.findById(1L)).thenReturn(Optional.empty());
////
////        assertThrows(AuthNotFoundException.class, () -> memberAuthService.assignAuthToMember(requestDto));
////    }
//}
