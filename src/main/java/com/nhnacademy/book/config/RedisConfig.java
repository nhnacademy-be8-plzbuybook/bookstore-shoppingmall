package com.nhnacademy.book.config;

import com.nhnacademy.book.skm.properties.SKMProperties;
import com.nhnacademy.book.skm.service.SecureKeyManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


// Refresh Token 을 저장하기 위한 Redis 설정
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final SecureKeyManagerService secureKeyManagerService;
    private final SKMProperties skmProperties;

    @Bean
    public String getOrderRedisHost() {
        return secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getHost());
    }

    @Bean
    public String getOrderRedisPort(){
        return secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getPort());
    }

    @Bean
    public String getOrderRedisPassword() {
        return secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getPassword());
    }

    @Bean
    public String getOrderRedisRange(){
        return secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getRange());
    }

    @Bean
    public String getCartRedisHost() {
        return secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getHost());
    }

    @Bean
    public String getCartRedisPort(){
        return secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getPort());
    }

    @Bean
    public String getCartRedisPassword() {
        return secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getPassword());
    }

    @Bean
    public String getCartRedisRange(){
        return secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getRange());
    }

    @Bean
    public RedisConnectionFactory orderRedisConnectionFactory() {
        String host = secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getHost());
        int port = Integer.parseInt(secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getPort()));
        String password = secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getPassword());
        int database = Integer.parseInt(secureKeyManagerService.fetchSecret(skmProperties.getOrderRedis().getRange()));

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);
        redisConfig.setDatabase(database);

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisConnectionFactory cartRedisConnectionFactory() {
        String host = secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getHost());
        int port = Integer.parseInt(secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getPort()));
        String password = secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getPassword());
        int database = Integer.parseInt(secureKeyManagerService.fetchSecret(skmProperties.getCartRedis().getRange()));

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);
        redisConfig.setDatabase(database);

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> orderRedisTemplate(RedisConnectionFactory orderRedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(orderRedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> cartRedisTemplate(RedisConnectionFactory cartRedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cartRedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
