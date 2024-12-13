package com.nhnacademy.book.book.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class JsonParser {

    private final ObjectMapper objectMapper;

    public JsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AladinBook parseAladinJson(String json) throws Exception {
        return objectMapper.readValue(json, AladinBook.class);
    }
}