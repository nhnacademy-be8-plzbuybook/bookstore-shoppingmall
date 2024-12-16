//package com.nhnacademy.book.booktest.repository;
//
//import com.nhnacademy.book.book.entity.*;
//import com.nhnacademy.book.book.repository.BookCategoryRepository;
//import com.nhnacademy.book.book.repository.BookRepository;
//import com.nhnacademy.book.book.repository.CategoryRepository;
//import com.nhnacademy.book.book.repository.PublisherRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//@DataJpaTest
//@ActiveProfiles("test")
//public class BookCategoryRepositoryTest {
//
//    @Autowired
//    private BookCategoryRepository bookCategoryRepository;
//    @Autowired
//    private BookRepository bookRepository;
//    @Autowired
//    private PublisherRepository publisherRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    public static Book book;
//    public static Book book2;
//
//
//    @BeforeEach
//    void setUp() {
//        Publisher publisher = new Publisher("Test Publisher");
//        publisherRepository.save(publisher);
//
//        Category rootCategory = new Category("국내도서", 1, null);
//        categoryRepository.save(rootCategory);  // 루트 카테고리 저장
//
//        Category subCategory1 = new Category("종교/역학", 2, rootCategory);
//        categoryRepository.save(subCategory1);  // 첫 번째 자식 카테고리 저장
//
//        Category subCategory2 = new Category("기독교(개신교)", 3, subCategory1);
//        categoryRepository.save(subCategory2);  // 두 번째 자식 카테고리 저장
//
//        Category anotherCategory = new Category("다른 카테고리",2, rootCategory);
//        categoryRepository.save(anotherCategory);
//        book = new Book(
//                publisher,                              // Publisher 설정
//                "Test Book Title",                      // 제목
//                "Test Book Index",                      // 목차
//                "Test Book Description",                // 설명
//                LocalDate.of(2023, 12, 13),             // 출판일
//                new BigDecimal("19.99"),                // 가격
//                "1234567890122",                        // ISBN
//                "1234567890123451"                      // ISBN-13
//        );
//
//        bookRepository.save(book);
//
//        BookCategory bookCategory = new BookCategory();
//        bookCategory.setCategory(subCategory2);
//        bookCategory.setBook(book);
//        bookCategoryRepository.save(bookCategory);
//
//        BookCategory bookCategory2 = new BookCategory();
//        bookCategory2.setCategory(anotherCategory);
//        bookCategory2.setBook(book);
//        bookCategoryRepository.save(bookCategory2);
//
//        book2 = new Book(
//                publisher,                              // Publisher 설정
//                "Test Book Title2",                      // 제목
//                "Test Book Index2",                      // 목차
//                "Test Book Description2",                // 설명
//                LocalDate.of(2023, 12, 13),             // 출판일
//                new BigDecimal("19.99"),                // 가격
//                "1234567890111",                        // ISBN
//                "1234567890123111"                      // ISBN-13
//        );
//
//        bookRepository.save(book2);
//        BookCategory bookCategory3 = new BookCategory();
//        bookCategory3.setCategory(subCategory2);
//        bookCategory3.setBook(book2);
//        bookCategoryRepository.save(bookCategory3);
//
//    }
//
//    //잘 등록된건가 확인차
//    @Test
//    void verifyBookCategoryData() {
//        List<BookCategory> bookCategories = bookCategoryRepository.findAll();
//        for (BookCategory bookCategory : bookCategories) {
//            System.out.println("Book ID: " + bookCategory.getBook().getBookId());
//            System.out.println("Category ID: " + bookCategory.getCategory().getCategoryId());
//        }
//    }
//
//    // 같은 책에 카테고리가 한 개 일때
//    @Test
//    void testFindCategoriesByBookId() {
//
//        List<Book> books = bookRepository.findAll();
//        List<Category> categories = bookCategoryRepository.findCategoriesByBookId(2L);
//
//        assertThat(categories).hasSize(1);
//    }
//
//
//    // 같은 책에 카테고리가 여러 개 일때 - 여기선 2개
//    @Test
//    void testFindCategoriesByBookId_2() {
//
//        List<Book> books = bookRepository.findAll();
//        List<Category> categories = bookCategoryRepository.findCategoriesByBookId(1L);
//
//        assertThat(categories).hasSize(2);
//    }
//
//    //카테고리에 속한 책 찾기
//    @Test
//    void testFindBooksByCategoryId() {
//
//        List<Book> books = bookCategoryRepository.findBooksByCategoryId(3L);
//
//        assertThat(books).hasSize(2);
//        assertThat(books).contains(book,book2);
//    }
//
//}
