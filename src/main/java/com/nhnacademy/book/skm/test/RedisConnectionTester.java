//package com.nhnacademy.book.skm.test;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RedisConnectionTester {
//    private final RedisTemplate<String, Object> orderRedisTemplate;
//    private final RedisTemplate<String, Object> cartRedisTemplate;
//
//    public RedisConnectionTester(
//            @Qualifier("redisTemplate") RedisTemplate<String, Object> orderRedisTemplate,
//            @Qualifier("cartRedisTemplate") RedisTemplate<String, Object> cartRedisTemplate) {
//        this.orderRedisTemplate = orderRedisTemplate;
//        this.cartRedisTemplate = cartRedisTemplate;
//    }
//
//    public void testRedisConnection() {
//        String orderKey = "orderTest2";
//        String orderValue = "Order Test Value";
//        String cartKey = "cartTest2";
//        String cartValue = "Cart Test Value";
//
//        orderRedisTemplate.opsForValue().set(orderKey, orderValue);
//        Object fetchedOrderValue = orderRedisTemplate.opsForValue().get(orderKey);
//        System.out.println("Order Redis: Key = " + orderKey + ", Value = " + fetchedOrderValue);
//
//        cartRedisTemplate.opsForValue().set(cartKey, cartValue);
//        Object fetchedCartValue = cartRedisTemplate.opsForValue().get(cartKey);
//        System.out.println("Cart Redis: Key = " + cartKey + ", Value = " + fetchedCartValue);
//
//
//    }
//}