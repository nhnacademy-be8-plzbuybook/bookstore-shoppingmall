package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.dto.QSellingBookSimpleResponseDto;
import com.nhnacademy.book.book.dto.SellingBookSimpleDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.book.book.entity.QBook.book;
import static com.nhnacademy.book.book.entity.QBookImage.bookImage;
import static com.nhnacademy.book.book.entity.QSellingBook.sellingBook;

@RequiredArgsConstructor
@Repository
public class SellingBookQueryRepository {

    private JPAQueryFactory queryFactory;

    Page<SellingBookSimpleDto> findSellingBooks(Pageable pageable, String sortBy, String sortDir) {

        List<SellingBookSimpleDto> sellingBooks = queryFactory
                .select(new QSellingBookSimpleResponseDto(
                        sellingBook.sellingBookId,
                        sellingBook.book.bookImages.get(0).imageUrl,
                        sellingBook.book.bookTitle
                ))
                .from(sellingBook)
                .innerJoin(book).on(sellingBook.book.eq(book))
                .innerJoin(bookImage).on(book.bookImages.get(0).eq(bookImage))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long count = queryFactory
                .select(sellingBook.count())
                .from(sellingBook)
                .innerJoin(book).on(sellingBook.book.eq(book))
                .innerJoin(bookImage).on(book.bookImages.get(0).eq(bookImage))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchOne();

        long total = (count != null) ? count : 0;

        return new PageImpl<>(sellingBooks, pageable, total);
    }


}
