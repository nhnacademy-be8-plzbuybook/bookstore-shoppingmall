package com.nhnacademy.book.config;

import com.nhnacademy.book.skm.properties.SKMProperties;
import com.nhnacademy.book.skm.service.SecureKeyManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedisConfigTest {

    private RedisConfig redisConfig;
    private SecureKeyManagerService secureKeyManagerService;
    private SKMProperties skmProperties;

    @BeforeEach
    void setUp() {
        secureKeyManagerService = mock(SecureKeyManagerService.class);
        skmProperties = mock(SKMProperties.class);

        SKMProperties.Order_redis orderRedis = mock(SKMProperties.Order_redis.class);
        when(orderRedis.getHost()).thenReturn("order.redis.host");
        when(orderRedis.getPort()).thenReturn("6379");
        when(orderRedis.getPassword()).thenReturn("order.redis.password");
        when(orderRedis.getRange()).thenReturn("0");

        SKMProperties.Cart_redis cartRedis = mock(SKMProperties.Cart_redis.class);
        when(cartRedis.getHost()).thenReturn("cart.redis.host");
        when(cartRedis.getPort()).thenReturn("6380");
        when(cartRedis.getPassword()).thenReturn("cart.redis.password");
        when(cartRedis.getRange()).thenReturn("1");

        when(skmProperties.getOrderRedis()).thenReturn(orderRedis);
        when(skmProperties.getCartRedis()).thenReturn(cartRedis);

        when(secureKeyManagerService.fetchSecret("order.redis.host")).thenReturn("127.0.0.1");
        when(secureKeyManagerService.fetchSecret("6379")).thenReturn("6379");
        when(secureKeyManagerService.fetchSecret("order.redis.password")).thenReturn("order-password");
        when(secureKeyManagerService.fetchSecret("0")).thenReturn("0");

        when(secureKeyManagerService.fetchSecret("cart.redis.host")).thenReturn("127.0.0.1");
        when(secureKeyManagerService.fetchSecret("6380")).thenReturn("6380");
        when(secureKeyManagerService.fetchSecret("cart.redis.password")).thenReturn("cart-password");
        when(secureKeyManagerService.fetchSecret("1")).thenReturn("1");

        redisConfig = new RedisConfig(secureKeyManagerService, skmProperties);
    }

    @Test
    void testOrderRedisConnectionFactory() {
        RedisConnectionFactory connectionFactory = redisConfig.orderRedisConnectionFactory();
        assertNotNull(connectionFactory);
        assert connectionFactory instanceof LettuceConnectionFactory;
    }

    @Test
    void testCartRedisConnectionFactory() {
        RedisConnectionFactory connectionFactory = redisConfig.cartRedisConnectionFactory();
        assertNotNull(connectionFactory);
        assert connectionFactory instanceof LettuceConnectionFactory;
    }

    @Test
    void testOrderRedisTemplate() {
        RedisConnectionFactory connectionFactory = redisConfig.orderRedisConnectionFactory();
        RedisTemplate<String, Object> redisTemplate = redisConfig.orderRedisTemplate(connectionFactory);
        assertNotNull(redisTemplate);
        assertNotNull(redisTemplate.getConnectionFactory());
    }

    @Test
    void testCartRedisTemplate() {
        RedisConnectionFactory connectionFactory = redisConfig.cartRedisConnectionFactory();
        RedisTemplate<String, Object> redisTemplate = redisConfig.cartRedisTemplate(connectionFactory);
        assertNotNull(redisTemplate);
        assertNotNull(redisTemplate.getConnectionFactory());
    }

    @Test
    void testRedisTemplatePrimary() {
        RedisConnectionFactory connectionFactory = redisConfig.orderRedisConnectionFactory();
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(connectionFactory);
        assertNotNull(redisTemplate);
        assertNotNull(redisTemplate.getConnectionFactory());
    }
}
