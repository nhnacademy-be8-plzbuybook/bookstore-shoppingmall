package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.request.SellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.AdminSellingBookRegisterDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/selling-books")
public class AdminSellingBookController {

    private final SellingBookService sellingBookService;

    @Autowired
    public AdminSellingBookController(SellingBookService sellingBookService) {
        this.sellingBookService = sellingBookService;
    }
    /**
     * 판매책 삭제 -> 특정 판매책 삭제 -> db 에서 실제로 삭제 관리자
     * 도서 삭제: DELETE /api/admin/selling-books/{sellingBookId}
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
     * 도서 수정: PUT /api/admin/selling-books/{sellingBookId}
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

//    /**
//     * 도서 등록: POST /api/admin/selling-books
//     * @param registerDto
//     * @return
//     */
//    @PostMapping
//    public ResponseEntity<SellingBookResponseDto> registerSellingBook(@RequestBody AdminSellingBookRegisterDto registerDto) {
//        return ResponseEntity.ok(sellingBookService.registerSellingBook(registerDto));
//    }

    /**
     *  관리자용 도서 목록 조회 (페이징 처리만)
     *  SellingBookResponseDto 를 반환하여 프론트엔드에서 필요한 정보를 전달.
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<AdminSellingBookRegisterDto>> adminGetBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sellingBookService.getBooks(pageable));
    }
}
