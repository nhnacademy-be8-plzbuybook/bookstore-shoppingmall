package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.entity.SellingBook.SellingBookStatus;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.BookRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nhnacademy.book.book.repository.CategoryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SellingBookService {

    private final SellingBookRepository sellingBookRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SellingBookService(SellingBookRepository sellingBookRepository, BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.sellingBookRepository = sellingBookRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * 판매책 등록
     * @param registerDto 판매책 등록 DTO
     * @return 등록된 판매책의 정보
     */
    @Transactional
    public SellingBookResponseDto registerSellingBook(SellingBookRegisterDto registerDto) {
        // Book 테이블에서 도서 정보 조회
        Book book = bookRepository.findById(registerDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + registerDto.getBookId()));

        // SellingBook 생성, 기본값 설정
        SellingBook sellingBook = new SellingBook();
        sellingBook.setBook(book);
        sellingBook.setSellingBookPrice(book.getBookPriceStandard()); // 기본값: 책의 정가
        sellingBook.setSellingBookPackageable(false); // 기본값: 선물 포장 불가
        sellingBook.setSellingBookStock(0); // 기본값: 재고 없음
        sellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING); // 기본값: 등록 대기 상태
        sellingBook.setUsed(false); // 기본값: 새 상품
        sellingBook.setSellingBookViewCount(0L); // 초기 조회수

        // 판매책 저장
        SellingBook savedSellingBook = sellingBookRepository.save(sellingBook);

        return toResponseDto(savedSellingBook);
    }


    /**
     * 판매책 수정 update -  특정 필드값 수정 가능 로직
     */
    @Transactional
    public SellingBookResponseDto updateSellingBook(Long sellingBookId, SellingBookRegisterDto updateDto) {
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
                .orElseThrow(() -> new SellingBookNotFoundException("SellingBook not found with ID: " + sellingBookId));

        // 특정 필드만 수정
        if (updateDto.getPrice() != null) {
            sellingBook.setSellingBookPrice(updateDto.getPrice());
        }
        if (updateDto.getPackageable() != null) {
            sellingBook.setSellingBookPackageable(updateDto.getPackageable());
        }
        if (updateDto.getStock() != null) {
            sellingBook.setSellingBookStock(updateDto.getStock());
        }
        if (updateDto.getUsed() != null) {
            sellingBook.setUsed(updateDto.getUsed());
        }
        if (updateDto.getStatus() != null) {
            sellingBook.setSellingBookStatus(updateDto.getStatus());
        }


        SellingBook updatedSellingBook = sellingBookRepository.save(sellingBook);
        return toResponseDto(updatedSellingBook);
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
    }


    // 조회(검색) 는 엘라스틱으로 !

    /**
     * 판매책 상세 조회
     */
    public SellingBookResponseDto getSellingBook(Long sellingBookId) {
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
                .orElseThrow(() -> new SellingBookNotFoundException("SellingBook not found with ID: " + sellingBookId));
        return toResponseDto(sellingBook);
    }


    /**
     * 판매책 목록 조회
     */
    public List<SellingBookResponseDto> getAllSellingBooks() {
        return sellingBookRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
    /**
     * 판매책 조회수 내림차순 정렬 조회 (조회수 높은 순)
     */
    public List<SellingBookResponseDto> getSellingBooksByViewCount(String sortDirection) {
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
    public List<SellingBookResponseDto> getSellingBooksByStatus(SellingBookStatus status) {
        return sellingBookRepository.findBySellingBookStatus(status)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * 특정 가격 범위 내 판매책 조회
     */
    public List<SellingBookResponseDto> getSellingBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return sellingBookRepository.findBySellingBookPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 조회수 이상 판매책 조회
     */
    public List<SellingBookResponseDto> getSellingBooksByViewCountGreaterThan(Long minViewCount) {
        return sellingBookRepository.findBySellingBookViewCountGreaterThanEqual(minViewCount)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 도서(Book) ID로 연결된 판매책 조회
     */
    public List<SellingBookResponseDto> getSellingBooksByBookId(Long bookId) {
        return sellingBookRepository.findByBook_BookId(bookId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * 카테고리별 도서 조회
     * @param categoryId
     * @return
     */
    public List<SellingBookResponseDto> getSellingBooksByCategory(Long categoryId) {
        List<SellingBook> sellingBooks = sellingBookRepository.findByCategoryIdOrParent(categoryId);
        return sellingBooks.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


//    public List<SellingBookResponseDto> findSellingBooksByCategory(Long categoryId) {
//        List<SellingBook> sellingBooks = sellingBookRepository.findByCategoryId(categoryId);
//
//        log.debug("카테고리 ID {}에서 찾은 판매책: {}", categoryId, sellingBooks);
//
//        if (sellingBooks.isEmpty()) {
//            throw new CategoryNotFoundException("No selling books found for category ID: " + categoryId + ". Please check the category or add related selling books.");
//        }
//
//        return sellingBooks.stream()
//                .map(this::toResponseDto)
//                .collect(Collectors.toList());
//    }



    /**
     * SellingBook -> SellingBookResponseDto 변환
     */
    private SellingBookResponseDto toResponseDto(SellingBook sellingBook) {
        return new SellingBookResponseDto(
                sellingBook.getSellingBookId(),
                sellingBook.getBook().getBookId(),
                sellingBook.getSellingBookPrice(),
                sellingBook.getSellingBookPackageable(),
                sellingBook.getSellingBookStock(),
                sellingBook.getSellingBookStatus(),
                sellingBook.getUsed(),
                sellingBook.getSellingBookViewCount()
        );
    }
}
