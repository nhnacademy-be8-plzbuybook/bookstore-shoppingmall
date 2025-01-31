package com.nhnacademy.book.book.repository;


import com.nhnacademy.book.book.dto.QSellingBookSimpleResponseDto;
import com.nhnacademy.book.book.dto.SellingBookSimpleResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.book.book.entity.QBook.book;
import static com.nhnacademy.book.book.entity.QBookImage.bookImage;
import static com.nhnacademy.book.book.entity.QLikes.likes;
import static com.nhnacademy.book.book.entity.QSellingBook.sellingBook;

@RequiredArgsConstructor
@Repository
public class SellingBookQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<SellingBookSimpleResponseDto> findSellingBooks(Pageable pageable, String sortBy, String sortDir) {

        JPQLQuery<Long> firstBookImageSubQuery = JPAExpressions
                .select(bookImage.bookImageId.min())
                .from(bookImage)
                .where(bookImage.book.eq(book));

        List<SellingBookSimpleResponseDto> sellingBooks = queryFactory
                .select(new QSellingBookSimpleResponseDto(
                        sellingBook.sellingBookId,
                        bookImage.imageUrl,
                        book.bookTitle
                ))
                .from(sellingBook)
                .innerJoin(book).on(sellingBook.book.eq(book))
                .leftJoin(bookImage).on(bookImage.book.eq(book).and(bookImage.bookImageId.eq(firstBookImageSubQuery)))
                .orderBy(sellingBookOrderSpecifier(sortBy, sortDir))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long count = queryFactory
                .select(sellingBook.count())
                .from(sellingBook)
                .innerJoin(book).on(sellingBook.book.eq(book))
                .leftJoin(bookImage).on(bookImage.book.eq(book).and(bookImage.bookImageId.eq(firstBookImageSubQuery)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchOne();

        long total = (count != null) ? count : 0;

        return new PageImpl<>(sellingBooks, pageable, total);
    }

    private OrderSpecifier<?> sellingBookOrderSpecifier(String sortBy, String sortDir) {
        if (sortBy == null) {
            return new OrderSpecifier<>(Order.DESC, book.bookPubDate);
        }
        return switch (sortBy) {
            case "low-price" -> new OrderSpecifier<>(Order.ASC, sellingBook.sellingBookPrice);
            case "high-price" -> new OrderSpecifier<>(Order.DESC, sellingBook.sellingBookPrice);
            case "likeCount" -> new OrderSpecifier<>(Order.DESC, likes.sellingBook.eq(sellingBook).count());
            default -> new OrderSpecifier<>(Order.DESC, book.bookPubDate);
        };
    }
}
