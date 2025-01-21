package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellinBookResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.elastic.repository.BookInfoRepository;
import com.nhnacademy.book.book.elastic.repository.SellingBookSearchRepository;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.*;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SellingBookService {

    private final SellingBookRepository sellingBookRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookImageRepository bookImageRepository; // 누락된 Repository 추가
    private final BookAuthorRepository bookAuthorRepository;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService; // 추가
    private final SellingBookSearchRepository sellingBookSearchRepository;
    private final BookInfoRepository bookInfoRepository;

    @Autowired
    public SellingBookService(SellingBookRepository sellingBookRepository, BookRepository bookRepository, CategoryRepository categoryRepository,
                              BookImageRepository bookImageRepository, BookAuthorRepository bookAuthorRepository, LikesRepository likesRepository,
                              MemberRepository memberRepository, MemberService memberService, SellingBookSearchRepository sellingBookSearchRepository, BookInfoRepository bookInfoRepository) {
        this.sellingBookRepository = sellingBookRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.bookImageRepository = bookImageRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.likesRepository = likesRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.sellingBookSearchRepository = sellingBookSearchRepository;
        this.bookInfoRepository = bookInfoRepository;
    }

    /**
     * 홈페이지 로드시 페이징 처리후 보여짐
     * @param pageable
     * @return
     */
    public Page<SellingBookAndBookResponseDto> getBooks(Pageable pageable, String sortBy) {
        if ("likeCount".equals(sortBy)) {
            // 좋아요 수 기준 정렬
            Page<SellingBook> books = sellingBookRepository.findAllWithLikeCount(pageable);
            return books.map(this::toResponseDto);
        }
        //기본 정렬
        Page<SellingBook> sellingBooks = sellingBookRepository.findAll(pageable);
        return sellingBooks.map(this::toResponseDto); // Page<SellingBook> -> Page<SellingBookResponseDto> 변환
    }




    /**
     * 판매책 수정 update -  특정 필드값 수정 가능 로직 (관리자)
     */
    @Transactional
    public SellinBookResponseDto updateSellingBook(Long sellingBookId, SellingBookRegisterDto updateDto) {
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
                .orElseThrow(() -> new SellingBookNotFoundException("SellingBook not found with ID: " + sellingBookId));

        // 특정 필드만 수정
        // 판매가 수정
        if (updateDto.getSellingBookPrice() != null) {
            sellingBook.setSellingBookPrice(updateDto.getSellingBookPrice());
        }
        // 선물 포장 가능 여부
        if (updateDto.getSellingBookPackageable() != null) {
            sellingBook.setSellingBookPackageable(updateDto.getSellingBookPackageable());
        }
        // 재고 수정

        if (updateDto.getSellingBookStock() != null) {
            sellingBook.setSellingBookStock(updateDto.getSellingBookStock());
        }
        // 도서 상태 수정
        if (updateDto.getSellingBookStatus() != null) {
            sellingBook.setSellingBookStatus(updateDto.getSellingBookStatus());
        }
        // 중고 여부 수정
        if (updateDto.getUsed() != null) {
            sellingBook.setUsed(updateDto.getUsed());
        }
        // 조회수 수정
        if (updateDto.getSellingBookViewCount() != null) {
            sellingBook.setSellingBookViewCount(updateDto.getSellingBookViewCount());
        }

        sellingBook = sellingBookRepository.save(sellingBook);

        // SellingBookRegisterDto로 변환 후 반환
        SellinBookResponseDto responseDto = new SellinBookResponseDto();
        responseDto.setSellingBookId(sellingBook.getSellingBookId());
        responseDto.setBookId(sellingBook.getBook().getBookId());
        responseDto.setSellingBookPrice(sellingBook.getSellingBookPrice());
        responseDto.setSellingBookPackageable(sellingBook.getSellingBookPackageable());
        responseDto.setSellingBookStock(sellingBook.getSellingBookStock());
        responseDto.setSellingBookStatus(sellingBook.getSellingBookStatus());
        responseDto.setSellingBookViewCount(sellingBook.getSellingBookViewCount());
        responseDto.setUsed(sellingBook.getUsed());


        return responseDto;
    }

    //TODO
    public void registerSellingBooks(SellingBookRegisterDto sellingBookRegisterDto) {
        // 1. 책 ID로 책 정보 조회
        Book book = bookRepository.findById(sellingBookRegisterDto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책 ID입니다."));

        // 2. 판매책 정보 생성
        SellingBook sellingBook = new SellingBook();
        sellingBook.setBook(book); // 책과 매핑
        sellingBook.setSellingBookPrice(sellingBookRegisterDto.getSellingBookPrice()); // 판매가
        sellingBook.setSellingBookPackageable(sellingBookRegisterDto.getSellingBookPackageable()); // 선물포장 가능 여부
        sellingBook.setSellingBookStock(sellingBookRegisterDto.getSellingBookStock()); // 재고
        sellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.safeValueOf(String.valueOf(sellingBookRegisterDto.getSellingBookStatus()))); // 도서 상태
        sellingBook.setSellingBookViewCount(sellingBookRegisterDto.getSellingBookViewCount() != null
                ? sellingBookRegisterDto.getSellingBookViewCount() : 0L); // 조회수 (기본값 0)
        sellingBook.setUsed(sellingBookRegisterDto.getUsed() != null ? sellingBookRegisterDto.getUsed() : false); // 중고 여부 (기본값 false)

        // 3. 판매책 저장
        sellingBookRepository.save(sellingBook);

        // 로그 출력 (디버깅 용도)
        log.info("판매책 등록 완료: {}", sellingBook);

    }



    /**
     * 판매책 삭제
     */
    @Transactional
    public void deleteSellingBook(Long sellingBookId) {
        if (!sellingBookRepository.existsById(sellingBookId)) {
            throw new SellingBookNotFoundException("SellingBook not found with ID: " + sellingBookId);
        }
        sellingBookRepository.deleteById(sellingBookId);
        bookInfoRepository.deleteBySellingBookId(sellingBookId);
        sellingBookRepository.flush(); // 데이터베이스와 동기화


    }

    /**
     * 판매책 상세 조회
     */
    public BookDetailResponseDto getSellingBook(Long sellingBookId) {

        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
                .orElseThrow(() -> new SellingBookNotFoundException("SellingBook not found with ID: " + sellingBookId));

        Book book = sellingBook.getBook();

        // BookImage를 Book 기준으로 검색
        BookImage bookImage = bookImageRepository.findByBook(book) // book으로 이미지 조회
                .orElseThrow(() -> new RuntimeException("BookImage not found for Book ID: " + book.getBookId()));

        // 카테고리 이름 가져오기
        List<String> categoryNames = categoryRepository.findCategoriesByBookId(book.getBookId())
                .stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());

        // 작가 이름 가져오기
        List<String> authorNames = bookAuthorRepository.findByBook_BookId(book.getBookId())
                .stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .collect(Collectors.toList());

        // 특정 판매책에 대한 좋아요 수 조회
        Long likeCount = likesRepository.countBySellingBookId(sellingBookId);


        return new BookDetailResponseDto(
                book.getBookId(),
                sellingBook.getSellingBookId(),
                book.getBookTitle(),
                book.getBookIndex(),
                book.getBookDescription(),
                book.getBookPubDate(),
                book.getBookPriceStandard(),
                sellingBook.getSellingBookPrice(), // 판매가 추가
                sellingBook.getSellingBookStock(),
                book.getBookIsbn13(),
                book.getPublisher().getPublisherId(),
                book.getPublisher().getPublisherName(), // 출판사 이름 추가
                bookImage.getImageUrl(),
                categoryNames,
                authorNames,
                sellingBook.getSellingBookStatus().name(), // 상태 추가
                likeCount
        );
    }


     /**
     * 판매책 조회수 내림차순 정렬 조회 (조회수 높은 순)
     */
    public List<SellingBookAndBookResponseDto> getSellingBooksByViewCount(String sortDirection) {
        Sort sort = Sort.by("sellingBookViewCount");
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        return sellingBookRepository.findAll(sort)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * 도서 상태별 판매책 조회
     */
    public List<SellingBookAndBookResponseDto> getSellingBooksByStatus(SellingBookStatus status) {
        return sellingBookRepository.findBySellingBookStatus(status)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * 특정 가격 범위 내 판매책 조회
     */
    public List<SellingBookAndBookResponseDto> getSellingBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return sellingBookRepository.findBySellingBookPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 도서 조회
     * @param categoryId
     * @return
     */
    public List<SellingBookAndBookResponseDto> getSellingBooksByCategory(Long categoryId) {
        List<SellingBook> sellingBooks = sellingBookRepository.findByCategoryIdOrParent(categoryId);
        return sellingBooks.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private SellingBookAndBookResponseDto toResponseDto(SellingBook sellingBook) {
        Book book = sellingBook.getBook();
        BookImage bookImage = bookImageRepository.findByBook(book).orElse(null);


        // 카테고리 정보 매핑
        List<String> categories = categoryRepository.findCategoriesByBookId(book.getBookId())
                .stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());

        // 작가 정보 매핑
        List<String> authors = bookAuthorRepository.findAuthorsByBookId(book.getBookId())
                .stream()
                .map(Author::getAuthorName) // Author의 authorName을 가져옴
                .collect(Collectors.toList());

        // 출판사 정보 가져오기
        String publisher = book.getPublisher().getPublisherName();

        return new SellingBookAndBookResponseDto(
                sellingBook.getSellingBookId(),
                book.getBookId(),
                sellingBook.getBookTitle(),
                sellingBook.getSellingBookPrice(),
                sellingBook.getSellingBookPackageable(),
                sellingBook.getSellingBookStock(),
                sellingBook.getSellingBookStatus(),
                sellingBook.getUsed(),
                sellingBook.getSellingBookViewCount(),
                bookImage != null ? bookImage.getImageUrl() : null,
                publisher,
                categories,
                authors
        );
    }

}
