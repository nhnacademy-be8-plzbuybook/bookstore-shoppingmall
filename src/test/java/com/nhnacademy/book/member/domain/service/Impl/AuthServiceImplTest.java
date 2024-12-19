package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Auth;
import com.nhnacademy.book.member.domain.dto.auth.AuthResponseDto;
import com.nhnacademy.book.member.domain.repository.auth.AuthRepository;
import com.nhnacademy.book.member.domain.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("새로운 권한을 생성하고 반환하는지")
    void testCreateAuth() {
        String authName = "ADMIN";
        Auth auth = new Auth();
        auth.setAuthName(authName);
        Mockito.when(authRepository.save(Mockito.any(Auth.class))).thenReturn(auth);

        AuthResponseDto createAuth = authService.createAuth(authName);

        assertNotNull(createAuth);
        assertEquals(authName, createAuth.getName());
    }

    @Test
    @DisplayName("모든 권한을 반환하는지")
    void testGetAllAuths_whenAuthsExist() {
        Auth adminAuth = new Auth(1L, "ADMIN");
        Auth userAuth = new Auth(2L, "USER");
        List<Auth> auths = Arrays.asList(adminAuth, userAuth);
        Mockito.when(authRepository.findAll()).thenReturn(auths);
        List<AuthResponseDto> result = authService.getAllAuths();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getName());
        assertEquals("USER", result.get(1).getName());
    }


    @Test
    @DisplayName("특정 authId를 반환하는지")
    void testGetAuthById() {
        Auth adminAuth = new Auth(1L, "ADMIN");

        Mockito.when(authRepository.findById(1L)).thenReturn(Optional.of(adminAuth));

        Optional<AuthResponseDto> result = authService.getAuthById(1L);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
    }

    @Test
    @DisplayName("정상적으로 수정하는지")
    void testUpdateAuth() {
        Auth adminAuth = new Auth(1L, "ADMIN");

        Mockito.when(authRepository.findById(1L)).thenReturn(Optional.of(adminAuth));
        Mockito.when(authRepository.save(adminAuth)).thenReturn(adminAuth);

        AuthResponseDto updatedAuth = authService.updateAuth(1L, "SUPER_ADMIN");

        assertNotNull(updatedAuth);
        assertEquals("SUPER_ADMIN", updatedAuth.getName());
    }

    @Test
    @DisplayName("정상적으로 삭제하는지")
    void testDeleteAuth() {
        Auth adminAuth = new Auth(1L, "ADMIN");

        Mockito.when(authRepository.findById(1L)).thenReturn(Optional.of(adminAuth));
        Mockito.doNothing().when(authRepository).delete(adminAuth);

        authService.deleteAuth(1L);

        Mockito.verify(authRepository, Mockito.times(1)).delete(adminAuth);
    }
}