package com.nhnacademy.book.book.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {

//    @Autowired
//    private RestHighLevelClient client;
//
//    public void insertData(String index, List<Map<String, Object>> dataList) throws IOException {
//        BulkRequest bulkRequest = new BulkRequest();
//
//        for (Map<String, Object> data : dataList) {
//            IndexRequest request = new IndexRequest(index)
//                    .source(data, XContentType.JSON);
//            bulkRequest.add(request);
//        }
//
//        client.bulk(bulkRequest, RequestOptions.DEFAULT);
//    }
}
