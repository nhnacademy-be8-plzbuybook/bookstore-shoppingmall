package com.nhnacademy.book.cartbook.controller;

import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.request.DeleteCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.request.UpdateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.service.CartBookMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookstore/carts")
public class CartBookController {
    private final CartBookMemberService cartBookMemberService;

    public CartBookController(CartBookMemberService cartBookMemberService) {
        this.cartBookMemberService = cartBookMemberService;
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

        createCartBookRequestDto = CreateCartBookRequestDto.builder()
                    .sellingBookId(createCartBookRequestDto.sellingBookId())
                    .quantity(createCartBookRequestDto.quantity())
                    .build();

        return ResponseEntity.ok(cartBookMemberService.createBookCartMember(createCartBookRequestDto, email));

    }

    @PutMapping()
    public ResponseEntity<Long> updateBookCart(
            @RequestBody UpdateCartBookRequestDto updateCartBookRequestDto,
            @RequestHeader(value = "X-USER-ID",required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.updateBookCartMember(updateCartBookRequestDto, email));

    }

    @DeleteMapping()
    public ResponseEntity<String> deleteBookCart(
            @RequestBody DeleteCartBookRequestDto deleteCartBookRequestDto,
            @RequestHeader(value = "X-USER-ID",required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.deleteBookCartMember(deleteCartBookRequestDto.cartBookId(), email));
    }


    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteAllBooks(
            @RequestHeader(value = "X-USER-ID",required = false) String email) {

        return ResponseEntity.ok(cartBookMemberService.deleteAllBookCartMember(email));
    }

}
