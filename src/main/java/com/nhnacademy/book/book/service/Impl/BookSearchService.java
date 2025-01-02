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

import java.math.BigDecimal;
import java.util.*;
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
        List<BookDocument> titleList = bookSearchRepository.findByBookTitleContaining(keyword);
        List<AuthorDocument> authorList = authorSearchRepository.findByAuthorNameContaining(keyword);
        List<CategoryDocument> categoryList = categorySearchRepository.findByCategoryNameContaining(keyword);

        // 중복 제거용 책 ID 저장
        Set<Long> bookIds = new HashSet<>();

        // 책 제목에서 책 ID 추출
        titleList.forEach(book -> bookIds.add(book.getBookId()));

        // 작가에서 책 ID 추출
        authorList.forEach(authorDoc -> {
            List<BookAuthorDocument> bookIdsFromAuthor = bookAuthorSearchRepository.findBookIdsByAuthorId(authorDoc.getAuthorId());
            bookIdsFromAuthor.forEach(bookAuthorDoc -> bookIds.add(bookAuthorDoc.getBookId()));
        });

        // 카테고리에서 책 ID 추출
        categoryList.forEach(categoryDoc -> {
            List<BookCategoryDocument> bookIdsFromCategory = bookCategorySearchRepository.findBookIdsByCategoryId(categoryDoc.getCategoryId());

            // bookIdsFromCategory에서 각 책 ID를 bookIds에 추가
            bookIdsFromCategory.forEach(bookCategoryDoc -> bookIds.add(bookCategoryDoc.getBookId()));
        });

        // 중복 제거 후 책 ID 리스트로 변환
        List<Long> pagedBookIds = new ArrayList<>(bookIds);

        // 페이징 처리: 원하는 페이지에 맞는 책 ID만 가져오기
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), pagedBookIds.size());

        // 판매책 정보 가져오기
        List<SellingBook> sellingBooks = sellingBookRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));

        // 카테고리, 작가, 이미지 정보 가져오기
        List<BookCategory> bookCategories = bookCategoryRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));
        List<BookImage> bookImages = bookImageRepository.findByBook_BookIdIn(pagedBookIds.subList(start, end));

        List<BookSearchResponseDto> responseDtos = pagedBookIds.subList(start, end).stream().map(bookId -> {
            // 책 정보를 찾기
            Book book = bookRepository.findById(bookId).orElse(null); // 책 ID에 해당하는 책 찾기 (필요시 repository 수정)

            // 해당 책 ID에 맞는 sellingBook 찾기
            List<SellingBook> sellingBooksList = sellingBookRepository.findByBook_BookId(bookId);

            SellingBook sellingBook = (sellingBooksList.isEmpty()) ? null : sellingBooksList.getFirst();

            // 카테고리, 작가, 이미지 정보 가져오기
            List<String> categories = bookCategories.stream()
                    .filter(bookCategory -> bookCategory.getBook().getBookId().equals(bookId))
                    .map(bookCategory -> bookCategory.getCategory().getCategoryName())
                    .collect(Collectors.toList());

            List<String> authors = bookAuthors.stream()
                    .filter(bookAuthor -> bookAuthor.getBook().getBookId().equals(bookId))
                    .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                    .collect(Collectors.toList());

            List<String> bookImageList = bookImages.stream()
                    .filter(bookImage -> bookImage.getBook().getBookId().equals(bookId))
                    .map(BookImage::getImageUrl)
                    .collect(Collectors.toList());

            // DTO 변환: null일 경우 기본값 처리
            return new BookSearchResponseDto(
                    book != null ? book.getBookId() : 0L,  // null일 경우 0 처리
                    book != null ? book.getBookTitle() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookIsbn13() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookPriceStandard() : BigDecimal.valueOf(0.0),  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookId() : 0L,  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookPrice() : BigDecimal.valueOf(0.0),  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookStock() : 0,  // null일 경우 0 처리
                    sellingBook != null ? sellingBook.getSellingBookStatus() : SellingBook.SellingBookStatus.safeValueOf(""),  // null일 경우 기본값 처리
                    authors,
                    categories,
                    bookImageList,
                    book != null ? book.getBookIndex() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookDescription() : "",  // null일 경우 공백 처리
                    book != null ? book.getBookPubDate() : null  // null일 경우 null 처리
            );
        }).collect(Collectors.toList());

        // 전체 아이템 수 계산 (중복된 책 제외)
        long totalItems = bookIds.size();

        // 페이지 반환
        return new PageImpl<>(responseDtos, pageable, totalItems);
    }




}
