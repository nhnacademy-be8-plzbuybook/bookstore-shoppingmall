package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        // Given: 테스트에 사용할 Publisher 엔티티 초기화
        publisher = new Publisher("Test Publisher");
    }

    // 테스트 1: Publisher 저장
    @Test
    void testSavePublisher() {
        // When: Publisher 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // Then: 저장된 Publisher의 ID 및 이름 검증
        assertNotNull(savedPublisher); // 저장된 객체가 null이 아닌지 확인
        assertNotNull(savedPublisher.getPublisherId()); // ID가 생성되었는지 확인
        assertEquals("Test Publisher", savedPublisher.getPublisherName()); // 이름이 올바르게 저장되었는지 확인
    }

    // 테스트 2: ID로 Publisher 조회
    @Test
    void testFindPublisherById() {
        // Given: Publisher 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: ID로 Publisher 조회
        Publisher foundPublisher = publisherRepository.findById(savedPublisher.getPublisherId()).orElse(null);

        // Then: 조회된 객체 검증
        assertNotNull(foundPublisher); // 조회된 객체가 null이 아닌지 확인
        assertEquals("Test Publisher", foundPublisher.getPublisherName()); // 이름이 올바른지 확인
    }

    // 테스트 3: 이름으로 Publisher 조회
    @Test
    void testFindPublisherByName() {
        // Given: Publisher 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: 이름으로 Publisher 조회
        Publisher foundPublisher = publisherRepository.findByPublisherName("Test Publisher").orElse(null);

        // Then: 조회된 객체 검증
        assertNotNull(foundPublisher); // 조회된 객체가 null이 아닌지 확인
        assertEquals("Test Publisher", foundPublisher.getPublisherName()); // 이름이 올바른지 확인
    }

    // 테스트 4: Publisher 업데이트
    @Test
    void testUpdatePublisher() {
        // Given: Publisher 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: Publisher 이름 업데이트
        savedPublisher.setPublisherName("Updated Publisher");
        Publisher updatedPublisher = publisherRepository.save(savedPublisher);

        // Then: 업데이트된 이름 검증
        assertNotNull(updatedPublisher); // 저장된 객체가 null이 아닌지 확인
        assertEquals("Updated Publisher", updatedPublisher.getPublisherName()); // 이름이 업데이트되었는지 확인
    }

    // 테스트 5: Publisher 삭제
    @Test
    void testDeletePublisher() {
        // Given: Publisher 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: Publisher 삭제
        publisherRepository.deleteById(savedPublisher.getPublisherId());

        // Then: 삭제된 Publisher는 조회되지 않아야 함
        boolean exists = publisherRepository.findById(savedPublisher.getPublisherId()).isPresent();
        assertFalse(exists); // Publisher가 삭제되었는지 확인
    }

    // 테스트 6: 존재하지 않는 이름으로 조회
    @Test
    void testFindNonExistentPublisherByName() {
        // When: 존재하지 않는 이름으로 조회
        Publisher foundPublisher = publisherRepository.findByPublisherName("Non Existent").orElse(null);

        // Then: 결과가 null이어야 함
        assertNull(foundPublisher); // 조회 결과가 null인지 확인
    }
}
