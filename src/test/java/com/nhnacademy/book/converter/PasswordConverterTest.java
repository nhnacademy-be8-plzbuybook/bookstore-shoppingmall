package com.nhnacademy.book.converter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @Test
    void convertToEntityAttribute_shouldThrowException() {
        String encryptedPassword = "encryptedPassword123";

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> passwordConverter.convertToEntityAttribute(encryptedPassword)
        );

        assertEquals("Password decryption is not supported.", exception.getMessage());
    }

}
