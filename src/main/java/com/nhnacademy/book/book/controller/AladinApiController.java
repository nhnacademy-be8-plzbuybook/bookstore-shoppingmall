package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.service.api.ApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class AladinApiController {

    private final ApiService aladinApiService;

    public AladinApiController(ApiService aladinApiService) {
        this.aladinApiService = aladinApiService;
    }

    /**
     * 기본 ItemList API를 통해 저장 -> 대량 저장
     */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncBooksFromListApi() {
        aladinApiService.saveBooksFromListApi();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sync/isbn")
    public ResponseEntity<Map<String, Object>> syncBooksByIsbns(@RequestBody List<String> isbns) {
        List<String> failedIsbns = aladinApiService.saveBooksByIsbnsDetailed(isbns); // 실패 ISBN 반환 메서드 사용
        if (failedIsbns.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "All books have been saved.", "failed", List.of()));
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(
                    Map.of("message", "Some books could not be saved.", "failed", failedIsbns));
        }
    }


    /**
     * 특정 ISBN(ItemId)를 기준으로 데이터베이스에 저장
     */
    @PostMapping("/sync/itemid/{isbn13}")
    public ResponseEntity<String> syncBookByIsbn(@PathVariable String isbn13) {
        boolean isSaved = aladinApiService.saveBooksByIsbns(List.of(isbn13));
        if (isSaved) {
            return ResponseEntity.ok("Book with ISBN " + isbn13 + " has been successfully saved.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No valid data found for ISBN " + isbn13 + ".");
        }
    }
}
