package com.nhnacademy.book.cartbook.controller;

import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.service.CartBookGuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/bookstore/guests/carts")
public class CartBookGuestController {

    private final CartBookGuestService cartBookGuestService;

    public CartBookGuestController(CartBookGuestService cartBookGuestService) {
        this.cartBookGuestService = cartBookGuestService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, Long>> createGuestCart(@RequestBody CreateCartBookRequestDto createCartBookRequestDto,
                                                             @RequestHeader("cart") String sessionId) {

        Long cartId = cartBookGuestService.AddToGuestCart(createCartBookRequestDto, sessionId);
        Map<String, Long> response = new HashMap<>();
        response.put("cartId", cartId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<ReadCartBookResponseDto>> getGuestCart(@RequestHeader("cart") String sessionId) {
        return ResponseEntity.ok(cartBookGuestService.getGuestCart(sessionId));
    }

    @PutMapping()
    public ResponseEntity<Map<String, Long>> updateGuestCartItem(@RequestParam Long cartId,
                                                                @RequestParam int quantity,
                                                                @RequestHeader("cart") String sessionId) {

        Long updatedCartId = cartBookGuestService.updateGuestCartItem(cartId, quantity, sessionId);
        Map<String, Long> response = new HashMap<>();
        response.put("cartId", updatedCartId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<Void> clearGuestCart(@RequestHeader("cart") String sessionId) {

        cartBookGuestService.clearGuestCart(sessionId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, Long>> removeItemFromGuestCart(@PathVariable("cartId") Long cartId,
                                            @RequestHeader("cart") String sessionId) {

        Long deletedCartId = cartBookGuestService.removeItemFromGuestCart(cartId, sessionId);
        Map<String, Long> response = new HashMap<>();
        response.put("cartId", deletedCartId);

        return ResponseEntity.ok(response);
    }
}
