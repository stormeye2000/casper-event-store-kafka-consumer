package com.stormeye.consumer.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonService {

    private final ObjectMapper mapper;

    public JsonService(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JsonNode getJson(final String event) throws JsonProcessingException {

        final String parsed = getData(event);

        return mapper.readTree(parsed);

    }

    private String getData(final String event){

        return event.substring(event.indexOf("event:'data:") + 12, event.length() - 1);

    }


}
