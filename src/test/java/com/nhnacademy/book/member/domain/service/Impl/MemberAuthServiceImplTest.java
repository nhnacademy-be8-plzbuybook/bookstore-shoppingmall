package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAuth;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthRequestDto;
import com.nhnacademy.book.member.domain.dto.auth.MemberAuthResponseDto;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.repository.auth.MemberAuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberAuthServiceImplTest {
    @Mock
    private MemberAuthRepository memberAuthRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthRepository authRepository;

    @InjectMocks
    private MemberAuthServiceImpl memberAuthService;

    private MemberAuthRequestDto requestDto;
    private Member member;
    private Auth auth;

    @BeforeEach
    public void setUp() {
        requestDto = new MemberAuthRequestDto();
        requestDto.setMemberId(1L);
        requestDto.setAuthId(1L);

        member = new Member();
        member.setMemberId(1L);

        auth = new Auth();
        auth.setAuthId(1L);

        lenient().when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        lenient().when(authRepository.findById(1L)).thenReturn(Optional.of(auth));
    }

    @Test
    @DisplayName("회원에게 권한 부여 성공")
    void assignAuthToMember_Success() {

        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(member);
        memberAuth.setAuth(auth);

        when(memberAuthRepository.save(any(MemberAuth.class)))
                .thenReturn(memberAuth);

        MemberAuthResponseDto response = memberAuthService.assignAuthToMember(requestDto);

        assertNotNull(response);
        assertEquals(1L, response.getMemberId());
        assertEquals(1L, response.getAuthId());
    }

    @Test
    @DisplayName("회원 권한 조회 성공")
    void getAuthNameByMember_Success() {
        Auth auth1 = new Auth(1L, "ADMIN");
        Auth auth2 = new Auth(2L, "USER");

        MemberAuth memberAuth1 = new MemberAuth();
        memberAuth1.setMember(member);
        memberAuth1.setAuth(auth1);

        MemberAuth memberAuth2 = new MemberAuth();
        memberAuth2.setMember(member);
        memberAuth2.setAuth(auth2);

        List<MemberAuth> memberAuthList = List.of(memberAuth1, memberAuth2);

        when(memberAuthRepository.findByMember(member)).thenReturn(memberAuthList);

        List<String> authNames = memberAuthService.getAuthNameByMember(1L);

        assertNotNull(authNames);
        assertEquals(2, authNames.size());
        assertTrue(authNames.contains("ADMIN"));
        assertTrue(authNames.contains("USER"));
    }

    @Test
    @DisplayName("회원 권한 수정 성공")
    void updateMemberAuth_Success() {
        Auth Auth1 = new Auth(1L, "ADMIN");
        Auth newAuth = new Auth(2L, "SUPER_ADMIN");

        MemberAuth existingMemberAuth = new MemberAuth();
        existingMemberAuth.setMember(member);
        existingMemberAuth.setAuth(Auth1);

        MemberAuth updatedMemberAuth = new MemberAuth();
        updatedMemberAuth.setMember(member);
        updatedMemberAuth.setAuth(newAuth);

        MemberAuthResponseDto expectedResponse = new MemberAuthResponseDto(1L, 2L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(authRepository.findById(2L)).thenReturn(Optional.of(newAuth));
        when(memberAuthRepository.findByMemberAndAuth(member, newAuth)).thenReturn(Optional.empty());
        when(memberAuthRepository.findByMember(member)).thenReturn(List.of(existingMemberAuth));
        when(memberAuthRepository.save(any(MemberAuth.class))).thenReturn(updatedMemberAuth);

        MemberAuthResponseDto response = memberAuthService.updateMemberAuth(new MemberAuthRequestDto(1L, 2L));

        assertNotNull(response);
        assertEquals(expectedResponse.getMemberId(), response.getMemberId());
        assertEquals(expectedResponse.getAuthId(), response.getAuthId());
    }



    @Test
    @DisplayName("회원 권한 삭제 성공")
    void deleteAuthFromMember_Success() {
        Auth authToDelete = new Auth();
        authToDelete.setAuthId(1L);
        authToDelete.setAuthName("ADMIN");

        MemberAuth existingMemberAuth = new MemberAuth();
        existingMemberAuth.setMember(member);
        existingMemberAuth.setAuth(authToDelete);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(authRepository.findById(1L)).thenReturn(Optional.of(authToDelete));
        when(memberAuthRepository.findByMemberAndAuth(member, authToDelete))
                .thenReturn(Optional.of(existingMemberAuth));

        memberAuthService.deleteAuthFromMember(1L, 1L);

        verify(memberAuthRepository, times(1)).delete(existingMemberAuth);
    }

}




