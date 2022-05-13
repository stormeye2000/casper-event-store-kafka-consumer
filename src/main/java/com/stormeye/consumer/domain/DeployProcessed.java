package com.stormeye.consumer.domain;


public class DeployProcessed extends AbstractEvent implements Event{
    public DeployProcessed(final String id, final String body) {
        super(id, body);
    }
}
