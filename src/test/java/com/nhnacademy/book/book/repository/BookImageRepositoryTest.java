package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.entity.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookImageRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookImageRepository bookImageRepository;

    private Book book;
    private Publisher publisher;
    private BookImage bookImage;

    @BeforeEach
    void setUp() {
        // Publisher 객체 초기화
        publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");
        publisherRepository.save(publisher); // Publisher 저장

        // Book 객체 초기화
        book = new Book();
        book.setBookTitle("Test Book");
        book.setBookIsbn13("1234567890123");
        book.setBookPriceStandard(BigDecimal.valueOf(10000.0)); // BigDecimal 값 설정
        book.setBookPubDate(LocalDate.now());
        book.setBookDescription("Test Description");
        book.setBookIndex("Test Index");
        book.setPublisher(publisher); // Publisher 설정
        bookRepository.save(book); // Book 저장

        // BookImage 객체 초기화
        bookImage = new BookImage();
        bookImage.setImageUrl("test-image.jpg");
        bookImage.setBook(book);
        bookImageRepository.save(bookImage); // BookImage 저장
    }

    @Test
    void testFindByBook() {
        Optional<BookImage> foundImage = bookImageRepository.findByBook(book);

        assertThat(foundImage).isPresent();
        assertThat(foundImage.get().getImageUrl()).isEqualTo("test-image.jpg");
    }

    @Test
    void testFindByBook_BookId() {
        List<BookImage> foundImages = bookImageRepository.findByBook_BookId(book.getBookId());

        assertThat(foundImages).isNotEmpty();
        assertThat(foundImages.get(0).getImageUrl()).isEqualTo("test-image.jpg");
    }

    @Test
    void testDeleteAllByBook() {
        bookImageRepository.deleteAllByBook(book);

        List<BookImage> foundImages = bookImageRepository.findByBook_BookId(book.getBookId());
        assertThat(foundImages).isEmpty();
    }

    @Test
    void testSaveBookImage() {
        BookImage newImage = new BookImage();
        newImage.setImageUrl("new-test-image.jpg");
        newImage.setBook(book);
        bookImageRepository.save(newImage);

        List<BookImage> foundImages = bookImageRepository.findByBook_BookId(book.getBookId());
        assertThat(foundImages).hasSize(2); // 기존 이미지 + 새 이미지
        assertThat(foundImages.get(1).getImageUrl()).isEqualTo("new-test-image.jpg");
    }
}
