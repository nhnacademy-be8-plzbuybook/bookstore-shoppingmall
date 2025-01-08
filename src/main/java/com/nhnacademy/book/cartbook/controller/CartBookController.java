package com.nhnacademy.book.cartbook.controller;

import com.nhnacademy.book.cartbook.dto.request.*;
import com.nhnacademy.book.cartbook.dto.response.*;
import com.nhnacademy.book.cartbook.service.CartBookGuestService;
import com.nhnacademy.book.cartbook.service.CartBookMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/bookstore/carts")
public class CartBookController {
    private final CartBookMemberService cartBookMemberService;
    private final CartBookGuestService cartBookGuestService;

    public CartBookController(CartBookMemberService cartBookMemberService, CartBookGuestService cartBookGuestService) {
        this.cartBookMemberService = cartBookMemberService;
        this.cartBookGuestService = cartBookGuestService;
    }


    @GetMapping("/{cartBookId}")
    public ResponseEntity<List<ReadCartBookResponseDto>> getBookCart(
            @PathVariable Long cartBookId) {
            //@RequestHeader(value = "X-USER-ID", required = false) String email

        List<ReadCartBookResponseDto> responses = cartBookGuestService.readAllCartBook(cartBookId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping()
    public ResponseEntity<List<ReadCartBookResponseDto>> getAllCartBooks
            (@RequestHeader(value = "X-USER-ID", required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.readAllCartMember(email));
    }

    @PostMapping()
    public ResponseEntity<Long> createBookCart(
            @RequestBody CreateCartBookRequestDto createCartBookRequestDto,
            @RequestHeader(value = "X-USER-ID", required = false) String email) {
        if(Objects.isNull(email)) {
            Long cartBookId = cartBookGuestService.createCartBook(createCartBookRequestDto.sellingBookId(),
                    null,
                    createCartBookRequestDto.quantity()
                    );
            return ResponseEntity.ok(cartBookId);
        } else {
            createCartBookRequestDto = CreateCartBookRequestDto.builder()
                    .sellingBookId(createCartBookRequestDto.sellingBookId())
                    .quantity(createCartBookRequestDto.quantity())
                    .build();
            return ResponseEntity.ok(cartBookMemberService.createBookCartMember(createCartBookRequestDto,email));
        }


    }

    @PutMapping()
    public ResponseEntity<Long> updateBookCart(
            @RequestBody UpdateCartBookRequestDto updateCartBookRequestDto,
            @RequestHeader(value = "X-USER-ID",required = false) String email) {
        if(Objects.isNull(email)) {
            cartBookGuestService.updateCartBook(
                    updateCartBookRequestDto.cartBookId(),
                    updateCartBookRequestDto.sellingBookId(),
                    updateCartBookRequestDto.quantity()
            );
            return ResponseEntity.ok(updateCartBookRequestDto.cartBookId());
        } else {
            return ResponseEntity.ok(cartBookMemberService.updateBookCartMember(updateCartBookRequestDto, email));
        }

    }

    @DeleteMapping()
    public ResponseEntity<String> deleteBookCart(
            @RequestBody DeleteCartBookRequestDto deleteCartBookRequestDto,
            @RequestHeader(value = "X-USER-ID",required = false) String email) {

        if (Objects.isNull(email)) {
            cartBookGuestService.deleteCartBook(
                    deleteCartBookRequestDto.cartBookId(),
                    deleteCartBookRequestDto.cartId()
            );
            return ResponseEntity.ok("delete success");
        } else {
            return ResponseEntity.ok(cartBookMemberService.deleteBookCartMember(deleteCartBookRequestDto.cartBookId(), email));
        }
    }


    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteAllBooks(
            @PathVariable(required = false) Long cartId,
            @RequestHeader(value = "X-USER-ID",required = false) String email) {
        if(Objects.isNull(email)) {
            String response = cartBookGuestService.deleteAllGuestBookCart(cartId);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(cartBookMemberService.deleteAllBookCartMember(email));
    }

}
