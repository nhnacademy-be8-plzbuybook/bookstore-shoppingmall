package com.nhnacademy.book.cartbook.controller;

import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.service.CartBookGuestService;
import jakarta.servlet.http.HttpServletRequest;
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
                                                             HttpServletRequest request) {
        Long cartId = cartBookGuestService.AddtoGuestCart(createCartBookRequestDto, request);
        Map<String, Long> response = new HashMap<>();
        response.put("cartId", cartId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<ReadCartBookResponseDto>> getGuestCart(HttpServletRequest request) {
        log.debug(request.getRequestId(), "getGuestCart");
        return ResponseEntity.ok(cartBookGuestService.getGuestCart(request));
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Map<String, Long>> updateGuestCartItem(@PathVariable Long cartId,
                                                                @RequestParam int quantity,
                                                                HttpServletRequest request) {
        Long updatedCartId = cartBookGuestService.updateGuestCartItem(cartId, quantity, request);
        Map<String, Long> response = new HashMap<>();
        response.put("cartId", updatedCartId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<Void> clearGuestCart(HttpServletRequest request) {
        cartBookGuestService.clearGuestCart(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, Long>> removeItemFromGuestCart(@PathVariable Long cartId,
                                                                    HttpServletRequest request) {
        Long deletedCartId = cartBookGuestService.removeItemFromGuestCart(cartId, request);
        Map<String, Long> response = new HashMap<>();
        response.put("cartId", deletedCartId);
        return ResponseEntity.ok(response);
    }
}
