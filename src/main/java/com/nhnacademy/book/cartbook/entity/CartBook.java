package com.nhnacademy.book.cartbook.entity;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.member.domain.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_book")
@AllArgsConstructor
public class CartBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "selling_book_id")
    private SellingBook sellingBook;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartBook() {

    }

    public CartBook(int quantity,SellingBook sellingBook, Cart cart) {
        this.quantity = quantity;
        this.sellingBook = sellingBook;
        this.cart = cart;
    }
}
