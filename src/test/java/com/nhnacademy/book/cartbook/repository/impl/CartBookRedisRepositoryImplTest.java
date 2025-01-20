package com.nhnacademy.book.cartbook.repository.impl;

import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CartBookRedisRepositoryImplTest {

    @Mock
    private RedisTemplate<String, Object> cartRedisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private CartBookRedisRepositoryImpl cartBookRedisRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cartRedisTemplate.opsForHash()).thenReturn(hashOperations); // HashOperations 모킹
    }

    @Test
    @DisplayName("장바구니 레디스 데이터 저장 테스트")
    void testCreate() {
        String hashName = "cart";
        Long id = 1L;
        ReadCartBookResponseDto dto = ReadCartBookResponseDto.builder()
                .cartBookId(1L)
                .sellingBookId(1L)
                .bookTitle("Test Book")
                .sellingBookPrice(BigDecimal.valueOf(10000))
                .imageUrl("http://example.com/image.jpg")
                .quantity(1)
                .sellingBookStock(10)
                .build();

        cartBookRedisRepository.create(hashName, id, dto);

        // Verify that the data was saved in Redis
        verify(hashOperations).put(hashName + ":", id.toString(), dto);
        verify(cartRedisTemplate).expire(hashName + ":", 1, TimeUnit.HOURS);
    }

    @Test
    @DisplayName("장바구니 레디스 데이터 업데이트 테스트")
    void testUpdate() {
        String hashName = "cart";
        Long id = 1L;
        int newQuantity = 2;

        ReadCartBookResponseDto existingDto = ReadCartBookResponseDto.builder()
                .cartBookId(1L)
                .sellingBookId(1L)
                .bookTitle("Test Book")
                .sellingBookPrice(BigDecimal.valueOf(10000))
                .imageUrl("http://example.com/image.jpg")
                .quantity(1)
                .sellingBookStock(10)
                .build();

        // Mocking the RedisTemplate behavior
        when(hashOperations.get(hashName + ":", id.toString())).thenReturn(existingDto);

        Long updatedId = cartBookRedisRepository.update(hashName, id, newQuantity);

        // Verify that the data was updated in Redis
        ArgumentCaptor<ReadCartBookResponseDto> dtoCaptor = ArgumentCaptor.forClass(ReadCartBookResponseDto.class);
        verify(hashOperations).put(eq(hashName + ":"), eq(id.toString()), dtoCaptor.capture());
        assertThat(dtoCaptor.getValue().quantity()).isEqualTo(newQuantity);
        verify(cartRedisTemplate).expire(hashName + ":", 1, TimeUnit.HOURS);
        assertThat(updatedId).isEqualTo(id);
    }

    @Test
    @DisplayName("장바구니 레디스 데이터 삭제 테스트")
    void testDelete() {
        String hashName = "cart";
        Long id = 1L;

        cartBookRedisRepository.delete(hashName, id);

        // Verify that the data was deleted from Redis
        verify(hashOperations).delete(hashName + ":", id.toString());
    }

    @Test
    @DisplayName("장바구니 레디스 데이터 전체 삭제 테스트")
    void testDeleteAll() {
        String hashName = "cart";

        cartBookRedisRepository.deleteAll(hashName);

        // Verify that all data was deleted from Redis
        verify(cartRedisTemplate).delete(hashName + ":");
    }

    @Test
    @DisplayName("장바구니 레디스 데이터 조회 테스트")
    void testReadAllHashName() {
        String hashName = "cart";

        ReadCartBookResponseDto dto1 = ReadCartBookResponseDto.builder()
                .cartBookId(1L)
                .sellingBookId(1L)
                .bookTitle("Test Book 1")
                .sellingBookPrice(BigDecimal.valueOf(10000))
                .imageUrl("http://example.com/image1.jpg")
                .quantity(1)
                .sellingBookStock(10)
                .build();

        ReadCartBookResponseDto dto2 = ReadCartBookResponseDto.builder()
                .cartBookId(2L)
                .sellingBookId(2L)
                .bookTitle("Test Book 2")
                .sellingBookPrice(BigDecimal.valueOf(20000))
                .imageUrl("http://example.com/image2.jpg")
                .quantity(1)
                .sellingBookStock(5)
                .build();

        // Mocking the RedisTemplate behavior
        when(hashOperations.values(hashName + ":")).thenReturn(List.of(dto1, dto2));

        List<ReadCartBookResponseDto> result = cartBookRedisRepository.readAllHashName(hashName);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    @DisplayName("장바구니 레디스 데이터 조회 테스트 - 데이터 있음")
    void testIsHit() {
        String hashName = "cart";

        when(cartRedisTemplate.hasKey(hashName + ":")).thenReturn(true);

        boolean result = cartBookRedisRepository.isHit(hashName);

        assertThat(result).isTrue();
        verify(cartRedisTemplate).hasKey(hashName + ":");
    }

    @Test
    @DisplayName("장바구니 레디스 데이터 조회 테스트 - 데이터 없음")
    void testIsMiss() {
        String hashName = "cart";

        when(cartRedisTemplate.hasKey(hashName + ":")).thenReturn(false);

        boolean result = cartBookRedisRepository.isMiss(hashName);

        assertThat(result).isTrue();
        verify(cartRedisTemplate).hasKey(hashName + ":");
    }

    @Test
    void testLoadData() {
        String hashName = "cart";
        ReadCartBookResponseDto dto1 = ReadCartBookResponseDto.builder()
                .cartBookId(1L)
                .sellingBookId(1L)
                .bookTitle("Test Book 1")
                .sellingBookPrice(BigDecimal.valueOf(10000))
                .imageUrl("http://example.com/image1.jpg")
                .quantity(1)
                .sellingBookStock(10)
                .build();

        ReadCartBookResponseDto dto2 = ReadCartBookResponseDto.builder()
                .cartBookId(2L)
                .sellingBookId(2L)
                .bookTitle("Test Book 2")
                .sellingBookPrice(BigDecimal.valueOf(20000))
                .imageUrl("http://example.com/image2.jpg")
                .quantity(1)
                .sellingBookStock(5)
                .build();

        List<ReadCartBookResponseDto> bookCartGuestResponses = List.of(dto1, dto2);

        cartBookRedisRepository.loadData(bookCartGuestResponses, hashName);

        // Verify that data was loaded in Redis
        verify(hashOperations, times(2)).put(eq(hashName + ":"), anyString(), any());
        verify(cartRedisTemplate, times(2)).expire(eq(hashName + ":"), eq(1L), eq(TimeUnit.HOURS));
    }

}