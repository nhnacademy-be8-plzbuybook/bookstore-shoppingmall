package com.nhnacademy.book.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void testPasswordEncoderBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SecurityConfig.class);

        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        assertThat(passwordEncoder).isNotNull();

        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);

        context.close();
    }

}