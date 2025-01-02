package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.BookResponseDto;
import com.nhnacademy.book.book.dto.response.BookSearchResponseDto;
import com.nhnacademy.book.book.elastic.document.*;
import com.nhnacademy.book.book.elastic.repository.*;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookSearchService {

    private final BookRepository bookRepository;
    private final SellingBookRepository sellingBookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookImageRepository bookImageRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final SellingBookBookSearchRepository sellingBookBookRepository;
    private final BookSearchRepository bookSearchRepository;
    private final BookAuthorSearchRepository bookAuthorSearchRepository;
    private final BookSearchCategoryRepository bookCategorySearchRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorSearchRepository authorSearchRepository;
    private final CategorySearchRepository categorySearchRepository;

    public List<BookDocument> getAllBooks(){

        return (List<BookDocument>) bookSearchRepository.findAll();
    }



//    public List<BookSearchResponseDto> searchBooksByKeyword(String keyword) {
//
//        // 1. 책 제목, 작가 이름, 카테고리 이름에서 책 ID 추출
//        List<BookDocument> title = bookSearchRepository.findByBookTitleContaining(keyword);
//        List<AuthorDocument> author = authorSearchRepository.findByAuthorNameContaining(keyword);
//        List<CategoryDocument> category = categorySearchRepository.findByCategoryNameContaining(keyword);
//
//        // Set을 사용하여 중복된 책 ID 제거
//        Set<Long> bookIds = new HashSet<>();
//
//        // 책 제목에서 책 ID 추출
//        title.forEach(book -> bookIds.add(book.getBookId()));
//
//        // 작가 이름에서 책 ID 추출 (작가와 책 관계)
//        author.forEach(authorDoc -> {
//            // 작가 ID로 책 ID 가져오기 (작가와 책 관계를 염두에 두고 처리)
//            List<BookAuthorDocument> bookIdsFromAuthor = bookAuthorSearchRepository.findBookIdsByAuthorId(authorDoc.getAuthorId());
//
//            // 책 ID만 추출하여 bookIds에 추가
//            bookIdsFromAuthor.forEach(bookAuthorDoc -> {
//                bookIds.add(bookAuthorDoc.getBookId()); // BookAuthorDocument에서 bookId를 가져와서 추가
//            });
//        });
//
//
//        // 카테고리 이름에서 책 ID 추출 (카테고리와 책 관계)
//        category.forEach(categoryDoc -> {
//            // 카테고리 ID로 책 ID 가져오기 (카테고리와 책 관계를 염두에 두고 처리)
//            List<Long> bookIdsFromCategory = bookCategorySearchRepository.findBookIdsByCategoryId(categoryDoc.getCategoryId());
//            bookIds.addAll(bookIdsFromCategory);
//        });
//
//        // 2. 판매책 정보 가져오기
//        List<SellingBook> sellingBooks = sellingBookRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));
//
//        // 3. 카테고리 정보 가져오기
//        List<BookCategory> bookCategories = bookCategoryRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));
//
//        // 4. 작가 정보 가져오기
//        List<BookAuthor> bookAuthors = bookAuthorRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));
//
//        List<BookImage> bookImages = bookImageRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));
//
//        // 5. BookSearchResponseDto로 변환
//        List<BookSearchResponseDto> responseDtos = new ArrayList<>();
//
//        for (SellingBook sellingBook : sellingBooks) {
//            Book book = sellingBook.getBook();
//
//            // 책의 카테고리 정보 가져오기
//            List<String> categories = bookCategories.stream()
//                    .filter(bookCategory -> bookCategory.getBook().getBookId().equals(book.getBookId()))
//                    .map(bookCategory -> bookCategory.getCategory().getCategoryName())
//                    .collect(Collectors.toList());
//
//            // 책의 작가 정보 가져오기
//            List<String> authors = bookAuthors.stream()
//                    .filter(bookAuthor -> bookAuthor.getBook().getBookId().equals(book.getBookId()))
//                    .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
//                    .collect(Collectors.toList());
//
//            List<String> bookImageList = bookImages.stream()
//                    .filter(bookImage -> bookImage.getBook().getBookId().equals(book.getBookId()))  // 책과 이미지가 연결된 조건
//                    .map(BookImage::getImageUrl)  // 이미지 URL만 추출
//                    .toList();
//
//
//            // BookSearchResponseDto로 매핑
//            BookSearchResponseDto dto = new BookSearchResponseDto(
//                    book.getBookId(),
//                    book.getBookTitle(),
//                    book.getBookIsbn13(),
//                    book.getBookPriceStandard(),
//                    sellingBook.getSellingBookId(),
//                    sellingBook.getSellingBookPrice(),
//                    sellingBook.getSellingBookStock(),
//                    sellingBook.getSellingBookStatus(),
//                    authors,  // 책의 작가 정보
//                    categories,
//                    bookImageList,
//                    book.getBookIndex(),
//                    book.getBookDescription(),
//                    book.getBookPubDate()
//                     // 책의 카테고리 정보
//            // 책 이미지 URL
//            );
//
//            responseDtos.add(dto);
//        }
//
//        return responseDtos;
//    }


    public Page<BookSearchResponseDto> searchBooksByKeyword(String keyword, Pageable pageable) {
        // 1. 책 제목, 작가 이름, 카테고리 이름에서 책 ID 추출 (페이징 처리된 결과)
        Page<BookDocument> titlePage = bookSearchRepository.findByBookTitleContaining(keyword, pageable);
        Page<AuthorDocument> authorPage = authorSearchRepository.findByAuthorNameContaining(keyword, pageable);  // 페이징 처리된 작가 검색
        Page<CategoryDocument> categoryPage = categorySearchRepository.findByCategoryNameContaining(keyword, pageable);  // 페이징 처리된 카테고리 검색

        // Set을 사용하여 중복된 책 ID 제거
        Set<Long> bookIds = new HashSet<>();

        // 책 제목에서 책 ID 추출 (페이징 처리된 결과)
        titlePage.forEach(book -> bookIds.add(book.getBookId()));

        // 작가 이름에서 책 ID 추출 (작가와 책 관계)
        authorPage.forEach(authorDoc -> {
            // 작가 ID로 책 ID 가져오기 (작가와 책 관계)
            List<BookAuthorDocument> bookIdsFromAuthor = bookAuthorSearchRepository.findBookIdsByAuthorId(authorDoc.getAuthorId());
            bookIdsFromAuthor.forEach(bookAuthorDoc -> bookIds.add(bookAuthorDoc.getBookId()));
        });

        // 카테고리 이름에서 책 ID 추출 (카테고리와 책 관계)
        categoryPage.forEach(categoryDoc -> {
            List<Long> bookIdsFromCategory = bookCategorySearchRepository.findBookIdsByCategoryId(categoryDoc.getCategoryId());
            bookIds.addAll(bookIdsFromCategory);
        });

        // 2. 판매책 정보 가져오기
        List<SellingBook> sellingBooks = sellingBookRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));

        // 3. 카테고리, 작가, 이미지 정보 가져오기
        List<BookCategory> bookCategories = bookCategoryRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));
        List<BookImage> bookImages = bookImageRepository.findByBook_BookIdIn(new ArrayList<>(bookIds));

        // 4. BookSearchResponseDto로 변환
        List<BookSearchResponseDto> responseDtos = new ArrayList<>();

        for (SellingBook sellingBook : sellingBooks) {
            Book book = sellingBook.getBook();

            // 책의 카테고리 정보 가져오기
            List<String> categories = bookCategories.stream()
                    .filter(bookCategory -> bookCategory.getBook().getBookId().equals(book.getBookId()))
                    .map(bookCategory -> bookCategory.getCategory().getCategoryName())
                    .collect(Collectors.toList());

            // 책의 작가 정보 가져오기
            List<String> authors = bookAuthors.stream()
                    .filter(bookAuthor -> bookAuthor.getBook().getBookId().equals(book.getBookId()))
                    .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                    .collect(Collectors.toList());

            // 책 이미지 URL 리스트 가져오기
            List<String> bookImageList = bookImages.stream()
                    .filter(bookImage -> bookImage.getBook().getBookId().equals(book.getBookId()))
                    .map(BookImage::getImageUrl)
                    .collect(Collectors.toList());

            // BookSearchResponseDto로 변환
            BookSearchResponseDto dto = new BookSearchResponseDto(
                    book.getBookId(),
                    book.getBookTitle(),
                    book.getBookIsbn13(),
                    book.getBookPriceStandard(),
                    sellingBook.getSellingBookId(),
                    sellingBook.getSellingBookPrice(),
                    sellingBook.getSellingBookStock(),
                    sellingBook.getSellingBookStatus(),
                    authors,
                    categories,
                    bookImageList,
                    book.getBookIndex(),
                    book.getBookDescription(),
                    book.getBookPubDate()
            );

            responseDtos.add(dto);
        }

        // 페이지 결과 반환 (현재 페이지에 해당하는 데이터만 포함)
        return new PageImpl<>(responseDtos, pageable, sellingBooks.size());
    }







}
