package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.SellingBookDocument;
import com.nhnacademy.book.book.entity.SellingBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface SellingBookSearchRepository extends ElasticsearchRepository<SellingBookDocument, Long> {


}
