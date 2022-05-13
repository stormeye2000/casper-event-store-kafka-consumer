package com.stormeye.consumer.domain;

/**
 * JSON storage of this emitted event
 * The id is the 'height' field
 */
public class BlockAdded extends AbstractEvent implements Event{

    public BlockAdded(final String id, final String body) {
        super(id, body);
    }
}
