package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.entity.SellingBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nhnacademy.book.book.service.Impl.SellingBookService;

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
     * 판매책 삭제 -> 특정 판매책 삭제 -> db 에서 실제로 삭제 관리자
     * @param sellingBookId
     * @return
     */
    @DeleteMapping("/{sellingBookId}")
    public ResponseEntity<Void> deleteSellingBook(@PathVariable Long sellingBookId) {
        sellingBookService.deleteSellingBook(sellingBookId);
        return ResponseEntity.ok().build();
    }

    /**
     * 판매책 수정 -> 판매책 정보( 가격, 재고, 상태 등 수정 각각 가능) 관리자
     * @param sellingBookId
     * @param updateDto
     * @return
     */
    @PutMapping("/{sellingBookId}")
    public ResponseEntity<SellingBookResponseDto> updateSellingBook(
            @PathVariable Long sellingBookId,
            @RequestBody SellingBookRegisterDto updateDto) {
        return ResponseEntity.ok(sellingBookService.updateSellingBook(sellingBookId, updateDto));
    }


    /**
     * 판매책 상세조회 -> 특정 판매책을 ID 로조회
     * @param sellingBookId
     * @return
     */
    @GetMapping("/{sellingBookId}")
    public ResponseEntity<SellingBookResponseDto> getSellingBook(@PathVariable Long sellingBookId) {
        return ResponseEntity.ok(sellingBookService.getSellingBook(sellingBookId));
    }

    /**
     * 조회수 정렬 판매책 조회 - 조회수 높은순, 낮은순
     */
    @GetMapping("/view-count")
    public ResponseEntity<List<SellingBookResponseDto>> getSellingBooksByViewCount(
            @RequestParam(defaultValue = "desc") String sortDirection) {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount(sortDirection));
    }


    /**
     * 도서 상태별 판매책 조회 - 판매중인, 품절된 그런거
     */
    @GetMapping("/status")
    public ResponseEntity<List<SellingBookResponseDto>> getSellingBooksByStatus(
            @RequestParam SellingBook.SellingBookStatus status) {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByStatus(status));
    }


    /**
     * 내림차순 만 되는거
     * @return
     */
    @GetMapping("/view-count/desc")
    public ResponseEntity<List<SellingBookResponseDto>> getSellingBooksByViewCountDesc() {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount("desc"));
    }


    /**
     * 올림만 되는ㄴ거
     * @return
     */
    @GetMapping("/view-count/asc")
    public ResponseEntity<List<SellingBookResponseDto>> getSellingBooksByViewCountAsc() {
        return ResponseEntity.ok(sellingBookService.getSellingBooksByViewCount("asc"));
    }


    /**
     * 카테고리별
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SellingBookResponseDto>> getSellingBooksByCategory(@PathVariable Long categoryId) {
        System.out.println("요청: 카테고리 ID " +  categoryId);
        List<SellingBookResponseDto> response = sellingBookService.getSellingBooksByCategory(categoryId);
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

