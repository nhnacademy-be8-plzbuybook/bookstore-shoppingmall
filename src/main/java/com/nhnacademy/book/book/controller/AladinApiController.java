package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.service.AladinApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")

public class AladinApiController {

    private final AladinApiService aladinApiService;

    public AladinApiController(AladinApiService aladinApiService) {
        this.aladinApiService = aladinApiService;
    }


    /**
     * ISBN 리스트를 받아서 개별 조회 후 저장
     */
    @PostMapping("/sync/isbn")
    public ResponseEntity<Void> syncBooksByIsbn(@RequestBody List<String> isbns) {
        aladinApiService.saveBooksByIsbns(isbns);
        return ResponseEntity.ok().build();
    }

    /**
     * 기본 ItemList API를 통해 저장 -> 대량 저장
     */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncBooksFromListApi() {
        aladinApiService.saveBooksFromListApi();
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 알라딘 식별번호(ItemId)를 기준으로 조회 및 저장
     */
    @PostMapping("/sync/itemid")
    public ResponseEntity<Void> syncBooksByItemIds(@RequestBody List<String> itemIds) {
        aladinApiService.saveBooksByItemIds(itemIds);
        return ResponseEntity.ok().build();
    }
    /**
     * 특정 시작 인덱스와 최대 결과 수를 기준으로 중복 최소화하여 도서 정보를 동적으로 조회.
     */
    @PostMapping("/sync/dynamic")
    public ResponseEntity<Void> syncBooksDynamically(@RequestParam int start, @RequestParam int maxResults) {
        aladinApiService.saveBooksFromListApiDynamic(start, maxResults);
        return ResponseEntity.ok().build();
    }

//    // 알라딘 API 호출 후 책 저장
//    @PostMapping("/sync/pathvaluer?배열로 받기 쿼리 파라미터에 담긴 횟수만큼 받기 -> isbn 10번 호출하면 ㅇㅇ/ 지금 있는 책은 똑같은거만 들어가니깡,,,,,,,/알라딘 ap i 알라딘에서 사용하는 식별번호로  ")
//    public ResponseEntity<Void> syncBooks() {
//        aladinApiService.saveBooksFromAladinApi();
//        return ResponseEntity.ok().build();
//    }
}
