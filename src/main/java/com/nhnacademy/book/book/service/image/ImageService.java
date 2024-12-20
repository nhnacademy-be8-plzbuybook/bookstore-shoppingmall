package com.nhnacademy.book.book.service.image;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.repository.BookImageRepository;
import com.nhnacademy.book.member.domain.Image;
import com.nhnacademy.book.member.domain.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImageService {

    private final BookImageRepository bookImageRepository;
    private final ImageRepository imageRepository;

    public ImageService(BookImageRepository bookImageRepository, ImageRepository imageRepository) {
        this.bookImageRepository = bookImageRepository;
        this.imageRepository = imageRepository;
    }

    /**
     * 이미지 URL을 저장하고, BookImage 테이블에 연결합니다.
     * @param book Book 엔티티
     * @param imageUrl 이미지 URL
     */
    @Transactional
    public void saveBookImage(Book book, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Image 테이블에 저장 또는 조회
            Image image = imageRepository.findByImagePath(imageUrl)
                    .orElseGet(() -> {
                        Image newImage = new Image();
                        newImage.setImagePath(imageUrl);
                        return imageRepository.save(newImage); // 여기서 저장한 엔티티 반환
                    });

            // 책 이미지 테이블에 저장
            BookImage bookImage = new BookImage();
            bookImage.setBook(book);
            bookImage.setImage(image); // Image 엔티티 설정
            bookImageRepository.save(image);

            log.debug("이미지 URL 저장 및 연결 완료: {}", imageUrl);
        }
    }
}
