package com.nhnacademy.book.book.service;


import com.nhnacademy.book.book.dto.response.AladinBookListResponse;
import com.nhnacademy.book.book.dto.response.AladinResponse;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Category;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AladinApiService {

    @Value("${aladin.api.url}")
    private String aladinApiUrl;

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;

    public AladinApiService(RestTemplate restTemplate, BookRepository bookRepository) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
    }

    // 책 리스트를 가져오는 메서드
    public void saveBooksFromAladinApi() {
        // 알라딘 API에서 책 리스트를 가져오는 URL
        String url = aladinApiUrl;  // 예시로 페이지당 10개의 책을 가져옵니다.

        // 알라딘 API에서 책 리스트를 받아옵니다.
        AladinBookListResponse response = restTemplate.getForObject(url, AladinBookListResponse.class);

        if (response != null && response.getBooks() != null) {
            // 각 책을 처리해서 DB에 저장
            for (AladinResponse bookData : response.getBooks()) {
                Book book = mapToBookEntity(bookData);
                bookRepository.save(book);  // 데이터베이스에 저장
            }
        } else {
            throw new RuntimeException("Failed to fetch books from Aladin API");
        }
    }

    // AladinResponse를 Book 엔티티로 변환
    private Book mapToBookEntity(AladinResponse response) {
        Book book = new Book();
        Publisher publisher = findPublisher(response.getPublisher().getPublisherName());
        book.setPublisher(publisher);
        book.setBookTitle(response.getBookTitle());
        book.setBookIndex(response.getBookIndex());
        book.setBookDescription(response.getBookDescription());
        book.setBookIsbn(response.getBookIsbn());
        book.setBookIsbn13(response.getBookIsbn13());
        book.setBookPriceStandard(response.getBookPriceStandard());
        book.setBookPubDate(response.getBookPubDate());

        // 카테고리 처리 예시
        Category category = new Category();
        category.setCategoryName(response.getCategory());

        return book;
    }


    private Publisher findPublisher(String publisherName) {
        // Publisher 찾는 로직 (DB에서 찾거나 새로 생성)
        Publisher publisher = new Publisher();
        publisher.setPublisherName(publisherName);
        return publisher;
    }
}
