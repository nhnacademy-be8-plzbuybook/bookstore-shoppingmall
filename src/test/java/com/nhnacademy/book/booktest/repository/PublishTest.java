package com.nhnacademy.book.booktest.repository;

import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PublishTest {

    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        // Publisher 엔티티 초기화
        publisher = new Publisher("Test Publisher");
    }

    @Test
    void testSavePublisher() {
        // Given: Publisher 객체를 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: 저장된 객체가 반환되면 ID가 할당되었는지 확인
        assertThat(savedPublisher).isNotNull();
        assertThat(savedPublisher.getPublisherId()).isNotNull();

        // Then: 저장된 Publisher의 이름이 예상한 값과 일치하는지 확인
        assertThat(savedPublisher.getPublisherName()).isEqualTo("Test Publisher");
    }

    @Test
    void testFindPublisherById() {
        // Given: Publisher 객체를 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: ID로 Publisher 조회
        Publisher foundPublisher = publisherRepository.findById(savedPublisher.getPublisherId()).orElse(null);

        // Then: 조회된 Publisher 객체가 null이 아니고, 이름이 예상한 값과 일치하는지 확인
        assertThat(foundPublisher).isNotNull();
        assertThat(foundPublisher.getPublisherName()).isEqualTo("Test Publisher");
    }

    @Test
    void testUpdatePublisher() {
        // Given: Publisher 객체를 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: Publisher 이름을 수정하고 저장
        savedPublisher.setPublisherName("Updated Publisher");
        Publisher updatedPublisher = publisherRepository.save(savedPublisher);

        // Then: 수정된 Publisher의 이름이 업데이트되었는지 확인
        assertThat(updatedPublisher).isNotNull();
        assertThat(updatedPublisher.getPublisherName()).isEqualTo("Updated Publisher");
    }

    @Test
    void testDeletePublisher() {
        // Given: Publisher 객체를 저장
        Publisher savedPublisher = publisherRepository.save(publisher);

        // When: Publisher 삭제
        publisherRepository.deleteById(savedPublisher.getPublisherId());

        // Then: 삭제된 Publisher는 더 이상 조회되지 않아야 한다
        assertThat(publisherRepository.findById(savedPublisher.getPublisherId()).isPresent()).isFalse();
    }
}

