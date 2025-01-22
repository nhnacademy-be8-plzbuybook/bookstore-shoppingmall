package com.nhnacademy.book.converter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class PasswordConverterTest {

    private PasswordConverter passwordConverter;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordConverter = new PasswordConverter();
        passwordEncoder = new BCryptPasswordEncoder();
    }


    @Test
    void convertToDatabaseColumn_shouldEncryptPassword() {
        String rawPassword = "securePassword123";

        String encryptedPassword = passwordConverter.convertToDatabaseColumn(rawPassword);

        log.info("{}", encryptedPassword);


        assertNotNull(encryptedPassword);
        assertNotEquals(rawPassword, encryptedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encryptedPassword));
    }

}

