package com.stormeye.consumer.domain;


public class DeployAccepted extends AbstractEvent implements Event {
    public DeployAccepted(final String id, final String body) {
        super(id, body);
    }
}
