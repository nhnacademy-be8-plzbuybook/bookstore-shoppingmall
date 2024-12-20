//package com.nhnacademy.book.book.service.image;
//
//import com.nhnacademy.book.book.entity.Book;
//import com.nhnacademy.book.book.entity.BookImage;
//import com.nhnacademy.book.book.repository.BookImageRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.*;
//
//class ImageServiceTest {
//
//    @Mock
//    private BookImageRepository bookImageRepository;
//
//    @InjectMocks
//    private ImageService imageService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @DisplayName("도서 이미지 URL 저장 성공 여부 확인")
//    @Test
//    void saveBookImage() {
//        // Given
//        Book book = new Book();
//        String imageUrl = "http://example.com/image.jpg";
//
//        // When
//        imageService.saveBookImage(book, imageUrl);
//
//        // Then
//        verify(bookImageRepository, times(1)).save(any(BookImage.class));
//    }
//}
