package com.stormeye.consumer.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains interactions with the kafka topics
 * Maps to Casper Event Types
 */
@Service
public class TopicsService {

    public String getTopic(final String event) {
        for(Topics topic : Topics.values()) {
            if (event.contains(topic.toString())) {
                return topic.toString();
            }
        }
        return null;
    }

    public List<String> getTopics(){
        return Stream.of(Topics.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }


}