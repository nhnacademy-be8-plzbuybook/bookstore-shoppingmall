package com.nhnacademy.book.book.elastic.repository;

import com.nhnacademy.book.book.elastic.document.BookDocument;
import com.nhnacademy.book.book.elastic.document.BookInfoDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookInfoRepository extends ElasticsearchRepository<BookInfoDocument, Long> {

//    @Query("{\"bool\": {\"should\": [" +
//            "{\"match\": {\"bookTitle\": \"?0\"}}, " +
//            "{\"match\": {\"authorNames\": \"?0\"}}, " +
//            "{\"match\": {\"categoryNames\": \"?0\"}}], " +
//            "\"minimum_should_match\": 1}}")
//    List<BookInfoDocument> searchBooksByKeyword(String keyword);
@Query("{\"bool\": {\"should\": [" +
        "{\"match\": {\"book_title\": {\"query\": \"?0\", \"operator\": \"or\", \"boost\": 3.0}}}, " +
        "{\"match\": {\"author_names\": {\"query\": \"?0\", \"operator\": \"or\", \"boost\": 1.5}}}, " +
        "{\"match\": {\"category_name\": {\"query\": \"?0\", \"operator\": \"or\"}}} ], " +
        "\"minimum_should_match\": 1}}")
List<BookInfoDocument> searchBooksByKeyword(String keyword);


    @Query("{\"match\": {\"author_name\": {\"query\": \"?0\", \"operator\": \"or\", \"boost\": 1.5}}} ")
    List<BookDocument> findByBookTitleContaining(String keyword);


//
//    // 'search_after'를 사용한 메서드 정의 (페이징 X, 리스트 반환)
//    @Query("{\n" +
//            "  \"query\": {\n" +
//            "    \"bool\": {\n" +
//            "      \"should\": [\n" +
//            "        { \"match\": { \"book_title\": { \"query\": \"?0\", \"operator\": \"or\" } } },\n" +
//            "        { \"match\": { \"author_name\": { \"query\": \"?0\", \"operator\": \"or\" } } },\n" +
//            "        { \"match\": { \"category_name\": { \"query\": \"?0\", \"operator\": \"or\" } } }\n" +
//            "      ]\n" +
//            "    }\n" +
//            "  },\n" +
//            "  \"sort\": [{ \"bookId\": \"asc\" }],\n" +
//            "  \"size\": ?1,\n" +
//            "  \"search_after\": ?2\n" +
//            "}")
//    List<BookInfoDocument> searchBooksByKeywordWithSearchAfter(String keyword, long searchAfter, int size);


}
