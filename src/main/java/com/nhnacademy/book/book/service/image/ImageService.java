package com.nhnacademy.book.book.service.image;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.repository.BookImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ImageService {

    private final BookImageRepository bookImageRepository;

    public ImageService(BookImageRepository bookImageRepository) {
        this.bookImageRepository = bookImageRepository;
    }

    /**
     * 이미지 URL을 저장합니다.
     * @param book Book 엔티티
     * @param imageUrl 이미지 URL
     */
    public void saveBookImage(Book book, String imageUrl) {
        // 유효한 URL인지 확인
        if (StringUtils.hasText(imageUrl)) {
            // BookImage 엔티티 생성
            BookImage bookImage = new BookImage();
            bookImage.setBook(book); // book 연관 설정
            bookImage.setImageUrl(imageUrl); // 이미지 URL 설정

            // BookImage 저장
            bookImageRepository.save(bookImage);
            log.debug("이미지 URL 저장 완료: {}, bookId: {}", imageUrl, book.getBookId());
        } else {
            log.warn("이미지 URL이 유효하지 않습니다. bookId: {}", book.getBookId());
        }
    }
}
