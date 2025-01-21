//package com.nhnacademy.book.config;
//
//import com.nhnacademy.book.skm.properties.SKMProperties;
//import com.nhnacademy.book.skm.service.SecureKeyManagerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.annotation.DirtiesContext;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = RedisConfig.class)
//class RedisConfigTest {
//
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;  // 기본 RedisTemplate
//
//    @Autowired
//    private RedisTemplate<String, Object> orderRedisTemplate;  // orderRedisTemplate
//
//    @Autowired
//    private RedisTemplate<String, Object> cartRedisTemplate;  // cartRedisTemplate
//
//    @Autowired
//    private RedisConnectionFactory orderRedisConnectionFactory;
//
//    @Autowired
//    private RedisConnectionFactory cartRedisConnectionFactory;
//
//    @MockBean
//    private SecureKeyManagerService secureKeyManagerService;
//
//    @MockBean
//    private SKMProperties skmProperties;
//
//    @BeforeEach
//    void setUp() {
//        // mock 반환값 설정
//        SKMProperties.Order_redis orderRedisMock = mock(SKMProperties.Order_redis.class);
//        when(orderRedisMock.getHost()).thenReturn("133.186.241.167");
//        when(skmProperties.getOrderRedis()).thenReturn(orderRedisMock);
//
//        SKMProperties.Cart_redis cartRedisMock = mock(SKMProperties.Cart_redis.class);
//        when(cartRedisMock.getHost()).thenReturn("133.186.241.167");
//        when(skmProperties.getCartRedis()).thenReturn(cartRedisMock);
//
//        when(secureKeyManagerService.fetchSecret(anyString())).thenReturn("someValue");
//    }
//
//    @Test
//    void testRedisTemplate() {
//        assertThat(redisTemplate).isNotNull();
//    }
//
//    @Test
//    void testOrderRedisTemplate() {
//        assertThat(orderRedisTemplate).isNotNull();
//    }
//
//    @Test
//    void testCartRedisTemplate() {
//        // Cart RedisTemplate 빈이 생성되었는지 확인
//        assertThat(cartRedisTemplate).isNotNull();
//    }
//
//    @Test
//    void testOrderRedisConnectionFactory() {
//        assertThat(orderRedisConnectionFactory).isNotNull();
//    }
//
//    @Test
//    void testCartRedisConnectionFactory() {
//        // Cart RedisConnectionFactory 빈이 생성되었는지 확인
//        assertThat(cartRedisConnectionFactory).isNotNull();
//    }
//}