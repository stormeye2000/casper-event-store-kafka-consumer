package com.stormeye.consumer.domain;

public class AbstractEvent {

    private String id;
    private String body;

    public AbstractEvent() {}

    public AbstractEvent(final String id, final String body) {
        this.id = id;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

}
