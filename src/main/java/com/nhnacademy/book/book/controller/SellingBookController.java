package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.SellingBookSimpleResponseDto;
import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellinBookResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/selling-books")
public class SellingBookController {

    private final SellingBookService sellingBookService;

    @GetMapping
    public ResponseEntity<Page<SellingBookSimpleResponseDto>> getBooks(Pageable pageable,
                                                                       @PageableDefault(size = 16, page = 0)
                                                                       @RequestParam(required = false) String sortBy,
                                                                       @RequestParam(required = false) String sortDir) {
        Page<SellingBookSimpleResponseDto> contents = sellingBookService.getBooks(pageable, sortBy, sortDir);
        return ResponseEntity.ok(contents);
    }


    /**
     * 판매도서 등록 기능 (관리자)
     *
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
     *
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
     *
     * @return
     */
    @GetMapping("/view-count/desc")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByViewCountDesc() {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount("desc"));
    }


    /**
     * 올림만 되는ㄴ거
     *
     * @return
     */
    @GetMapping("/view-count/asc")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByViewCountAsc() {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount("asc"));
    }


    /**
     * 카테고리별
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SellingBookAndBookResponseDto>> getSellingBooksByCategory(@PathVariable Long categoryId) {
        System.out.println("요청: 카테고리 ID " + categoryId);
        List<SellingBookAndBookResponseDto> response = sellingBookService.getSellingBooksByCategory(categoryId);
        log.debug("응답 크기: {}", response.size());
        return ResponseEntity.ok(response);
    }

//    /**
//     * 판매책 가격과 임시 수량을 곱한 총 금액 반환
//     *
//     * @param sellingBookId 판매책 ID
//     * @param quantity 주문 수량 (임시 설정)
//     * @return 가격 * 수량의 총합
//     */
//    @GetMapping("/{sellingBookId}/calculate-price")
//    public ResponseEntity<BigDecimal> calculateOrderPrice(
//            @PathVariable Long sellingBookId,
//            @RequestParam int quantity) {
//        BigDecimal totalPrice = sellingBookService.calculateOrderPrice(sellingBookId, quantity);
//        return ResponseEntity.ok(totalPrice);
//    }
//

}

