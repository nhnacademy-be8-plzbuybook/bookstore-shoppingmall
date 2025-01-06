package com.nhnacademy.book.cartbook.controller;

import com.nhnacademy.book.cartbook.dto.request.*;
import com.nhnacademy.book.cartbook.dto.response.*;
import com.nhnacademy.book.cartbook.service.CartBookMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookstore/carts")
public class CartBookMemberController {
    private final CartBookMemberService cartBookMemberService;

    public CartBookMemberController(CartBookMemberService cartBookMemberService) {
        this.cartBookMemberService = cartBookMemberService;
    }


    @GetMapping("/{cartBookId}")
    public ResponseEntity<ReadMemberCartBookResponseDto> getBookCart(
            @PathVariable Long cartBookId,
            @RequestHeader(value = "X-USER-ID", required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.readMemberCartBook(cartBookId, email));
    }

    @GetMapping()
    public ResponseEntity<List<ReadAllMemberCartBookResponseDto>> getAllBooks
            (@RequestHeader(value = "X-USER-ID", required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.readAllCartMember(email));
    }

    @PostMapping()
    public ResponseEntity<CreateMemberCartBookResponseDto> createBookCart(
            @RequestBody CreateCartBookRequestDto createCartBookRequestDto,
            @RequestHeader(value = "X-USER-ID", required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.createBookCartMember(createCartBookRequestDto, email));

    }

    @PutMapping()
    public ResponseEntity<UpdateMemberCartBookResponseDto> updateBookCart(
            @RequestBody UpdateCartBookRequestDto updateCartBookRequestDto,
            @RequestHeader(value = "X-USER-ID",required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.updateBookCartMember(updateCartBookRequestDto, email));
    }

    @DeleteMapping("/{cartBookId}")
    public ResponseEntity<Long> deleteBookCart(
            @PathVariable Long cartBookId,
            @RequestHeader(value = "X-USER-ID",required = false) String email) {

        return cartBookMemberService.deleteBookCartMember(cartBookId, email);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAllBooks(
            @RequestHeader(value = "X-USER-ID",required = false) String email) {

        return cartBookMemberService.deleteAllBookCartMember(email);
    }

}
