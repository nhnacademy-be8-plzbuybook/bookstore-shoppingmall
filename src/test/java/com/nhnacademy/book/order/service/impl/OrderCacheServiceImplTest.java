package com.nhnacademy.book.order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.data.redis.core.RedisTemplate;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderCacheServiceImplTest {
    private RedisServer redisServer;
    private RedissonClient redissonClient;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    private OrderCacheServiceImpl orderCacheService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private WrappingPaperRepository wrappingPaperRepository;
    @Mock
    private SellingBookRepository sellingBookRepository;
    private static final String TEST_KEY = "stock:book";
    private static final int THREAD_LIMIT = 100;

    @BeforeAll
    void startRedis() {
        redisServer = new RedisServerBuilder()
                .port(6380)
                .setting("maxheap 512mb")
                .build();

        redisServer.start();

        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6380");
        config.setCodec(StringCodec.INSTANCE);
        redissonClient = Redisson.create(config);

        orderCacheService = new OrderCacheServiceImpl(objectMapper, redisTemplate, wrappingPaperRepository, sellingBookRepository, redissonClient);
    }

    @BeforeEach
    void setUp() {
        // Redisson을 통해 데이터를 설정하여 decreaseWithLock 메서드와 일관성 유지
        redissonClient.getBucket(TEST_KEY).set(100L); // Long 타입으로 설정
    }

    @AfterEach
    void clearRedis() {
        redissonClient.getKeys().flushall();
    }

    @AfterAll
    void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    @DisplayName("동시에_100명_재고감소_동시성_문제")
    @Test
    void decrease() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_LIMIT);
        CountDownLatch latch = new CountDownLatch(THREAD_LIMIT);

        long start = System.currentTimeMillis();
        for (int i = 0; i < THREAD_LIMIT; i++) {
            executor.submit(() -> {
                try {
                    orderCacheService.decrease(TEST_KEY, 1);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("소요시간" + (end - start) + "ms");

        Long finalStock = (Long) redissonClient.getBucket(TEST_KEY).get();
        Assertions.assertNotEquals(0L, finalStock);
    }

    @DisplayName("동시에_100명_재고감소_동시성제어_분산락")
    @Test
    void testDecreaseWithLock_Concurrency() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_LIMIT);
        CountDownLatch latch = new CountDownLatch(THREAD_LIMIT);

        long start = System.currentTimeMillis();
        for (int i = 0; i < THREAD_LIMIT; i++) {
            executor.submit(() -> {
                try {
                    orderCacheService.decreaseWithLock(TEST_KEY, 1);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("소요시간" + (end - start) + "ms");

        // Redisson을 통해 최종 값 확인
        Long finalStock = (Long) redissonClient.getBucket(TEST_KEY).get();
        Assertions.assertEquals(0L, finalStock);
    }

    @DisplayName("동시에_100명_재고감소_동시성제어_lua스크립트")
    @Test
    void testDecreaseWithLuaScript() throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_LIMIT);
        CountDownLatch latch = new CountDownLatch(THREAD_LIMIT);

        long start = System.currentTimeMillis();
        for (int i = 0; i < THREAD_LIMIT; i++) {
            executor.submit(() -> {
                try {
                    orderCacheService.decreaseWithLuaScript(TEST_KEY, 1);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("소요시간" + (end - start) + "ms");

        // Redisson을 통해 최종 값 확인
        Object result = redissonClient.getBucket(TEST_KEY).get();
        Long finalStock = Long.valueOf((String) result);
        Assertions.assertEquals(0L, finalStock);
    }
}