package com.nhnacademy.book.cartbook.repository;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.cartbook.entity.CartBook;
import com.nhnacademy.book.member.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartBookRepository extends JpaRepository<CartBook, Long> {
    List<CartBook> findAllByCart(Cart cart);

    void deleteByCart(Cart cart);

    Optional<CartBook> findBySellingBookAndCart(SellingBook sellingBook, Cart cart);

    Optional<CartBook> findBySellingBook_SellingBookIdAndCart_CartId (Long sellingBookId, Long CartId);

    boolean existsCartBookBySellingBook_SellingBookIdAndCart(Long sellingBookSellingBookId, Cart cart);

    List<CartBook> findAllByCart_CartId(Long cartId);

    List<CartBook> findByCart(Cart cart);
}
