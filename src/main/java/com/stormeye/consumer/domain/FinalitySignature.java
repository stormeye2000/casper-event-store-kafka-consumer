package com.stormeye.consumer.domain;

public class FinalitySignature extends AbstractEvent implements Event {
    public FinalitySignature(final String id, final String body) {
        super(id, body);
    }
}
