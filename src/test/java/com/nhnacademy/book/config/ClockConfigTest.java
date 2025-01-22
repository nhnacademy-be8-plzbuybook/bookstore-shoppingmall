package com.nhnacademy.book.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClockConfigTest {

    @Test
    @DisplayName("Clock 빈 생성 테스트")
    void clockBeanCreationTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ClockConfig.class);

        Clock clock = context.getBean(Clock.class);

        assertEquals(Clock.systemDefaultZone().getZone(), clock.getZone(), "Clock의 Zone이 시스템 기본 Zone과 동일합니다.");

        context.close();
    }

}