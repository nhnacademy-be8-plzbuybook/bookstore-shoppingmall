package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellinBookResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.entity.SellingBook;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/selling-books")
public class SellingBookController {

    private final SellingBookService sellingBookService;

    @Autowired
    public SellingBookController(SellingBookService sellingBookService) {
        this.sellingBookService = sellingBookService;
    }

    /**
     * index 화면 페이징 한후 로드
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     */
    //유저
    @GetMapping
    public ResponseEntity<Page<SellingBookAndBookResponseDto>> getBooks(
            @RequestParam(defaultValue = "0") int page,         // 기본 페이지 번호
            @RequestParam(defaultValue = "16") int size,        // 페이지 크기
            @RequestParam(defaultValue = "sellingBookId") String sortBy,  // 정렬 기준
            @RequestParam(defaultValue = "desc") String sortDir // 정렬 방향
    ) {
        Pageable pageable;
        if ("likeCount".equals(sortBy)) {
            // 좋아요 수 기준 정렬
            pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(sellingBookService.getBooks(pageable, sortBy));
        } else {
            // 일반 정렬 기준 적용
            String sortField;
            switch (sortBy) {
                case "new":           // 신상품 (출판 날짜 기준)
                    sortField = "book.bookPubDate";
                    break;
                case "low-price":     // 최저가
                    sortField = "sellingBookPrice";
                    sortDir = "asc"; // 강제로 오름차순
                    break;
                case "high-price":    // 최고가
                    sortField = "sellingBookPrice";
                    sortDir = "desc"; // 강제로 내림차순
                    break;
                default:              // 기본값 (ID 정렬)
                    sortField = "sellingBookId";
            }

            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortField));
            return ResponseEntity.ok(sellingBookService.getBooks(pageable, sortBy));
        }
    }



    /**
     * 판매도서 등록 기능 (관리자)
     * @param sellingBookRegisterDto
     * @return
     */

    @PostMapping
    public ResponseEntity<SellingBookRegisterDto> registerSellingBooks(
            @RequestBody @Valid SellingBookRegisterDto sellingBookRegisterDto) {

        log.info("Received DTO: {}", sellingBookRegisterDto); // DTO 데이터 확인

        // 서비스 호출
        sellingBookService.registerSellingBooks(sellingBookRegisterDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(sellingBookRegisterDto);
    }

    /**
     * 판매책 삭제 -> 특정 판매책 삭제 -> db 에서 실제로 삭제 관리자
     * @param sellingBookId
     * @return
     */ // 수정완료
    @DeleteMapping("/{sellingBookId}")
    public ResponseEntity<Void> deleteSellingBook(@PathVariable Long sellingBookId) {
        sellingBookService.deleteSellingBook(sellingBookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



    @PutMapping("/{sellingBookId}")
    public ResponseEntity<SellinBookResponseDto> updateSellingBook(
            @PathVariable Long sellingBookId,
            @RequestBody SellingBookRegisterDto updateDto) {
        return ResponseEntity.ok(sellingBookService.updateSellingBook(sellingBookId, updateDto));
    }



    /**
     * 판매책 상세조회 -> 특정 판매책을 ID 로조회
     *
     * @param sellingBookId
     * @return
     */
    @GetMapping("/{sellingBookId}")
    public ResponseEntity<BookDetailResponseDto> getSellingBook(@PathVariable Long sellingBookId) {
        return ResponseEntity.ok(sellingBookService.getSellingBook(sellingBookId));
    }

    /**
     * 조회수 정렬 판매책 조회 - 조회수 높은순, 낮은순
     */
    @GetMapping("/view-count")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByViewCount(
            @RequestParam(defaultValue = "desc") String sortDirection) {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount(sortDirection));
    }


    /**
     * 도서 상태별 판매책 조회 - 판매중인, 품절된 그런거
     */
    @GetMapping("/status")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByStatus(
            @RequestParam SellingBook.SellingBookStatus status) {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByStatus(status));
    }


    /**
     * 내림차순 만 되는거
     * @return
     */
    @GetMapping("/view-count/desc")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByViewCountDesc() {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount("desc"));
    }


    /**
     * 올림만 되는ㄴ거
     * @return
     */
    @GetMapping("/view-count/asc")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByViewCountAsc() {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount("asc"));
    }




    /**
     * 카테고리별
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByCategory(@PathVariable Long categoryId) {
        System.out.println("요청: 카테고리 ID " +  categoryId);
        List<SellingBookAndBookResponseDto> response = sellingBookService.getSellingBooksByCategory(categoryId);
        log.debug("응답 크기: {}", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * 판매책 가격과 임시 수량을 곱한 총 금액 반환
     *
     * @param sellingBookId 판매책 ID
     * @param quantity 주문 수량 (임시 설정)
     * @return 가격 * 수량의 총합
     */
    @GetMapping("/{sellingBookId}/calculate-price")
    public ResponseEntity<BigDecimal> calculateOrderPrice(
            @PathVariable Long sellingBookId,
            @RequestParam int quantity) {
        BigDecimal totalPrice = sellingBookService.calculateOrderPrice(sellingBookId, quantity);
        return ResponseEntity.ok(totalPrice);
    }


}

