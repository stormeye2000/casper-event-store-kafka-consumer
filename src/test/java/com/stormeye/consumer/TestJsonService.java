package com.stormeye.consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.stormeye.consumer.service.JsonService;

@SpringBootTest(classes = {JsonService.class})
@EnableAutoConfiguration
public class TestJsonService {

    @Autowired
    private JsonService jsonService;

    private final String event = "{emitter:'http://65.21.235.219:9999/events/sigs', event:'data:{\"FinalitySignature\":{\"block_hash\":\"6e1165b9e2f0e3b30178c08aa6332448287e125e2c71757b595355945e14adef\",\"era_id\":5203,\"signature\":\"015737eee7bfea11eb9d3fd419eb334f400ed2f8ff520952fd26ff176730a644e77d41e2cfd4cc6f281e3b058e12aa3939e3c458e23d95cdcbbfe5d9ccd5d86b07\",\"public_key\":\"01d949a3a1963db686607a00862f79b76ceb185fc134d0aeedb686f1c151f4ae54\"}}'}";

    @Test
    void testEventToJson() throws JsonProcessingException {

        JsonNode json = jsonService.getJson(event);

        assertNotNull(json);

    }



}
