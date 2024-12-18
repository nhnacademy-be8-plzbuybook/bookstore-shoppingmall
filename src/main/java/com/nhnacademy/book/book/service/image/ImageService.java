package com.nhnacademy.book.book.service.image;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.repository.BookImageRepository;
import org.springframework.stereotype.Service;

@Service
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
        if (imageUrl != null && !imageUrl.isEmpty()) {
            BookImage bookImage = new BookImage();
            bookImage.setBook(book);
            bookImage.setImageId((long) imageUrl.hashCode()); // 간단한 해시값으로 ID 생성

            bookImageRepository.save(bookImage); // BookImageRepository 사용
            System.out.println("이미지 URL 저장: " + imageUrl);
        }
    }

}
