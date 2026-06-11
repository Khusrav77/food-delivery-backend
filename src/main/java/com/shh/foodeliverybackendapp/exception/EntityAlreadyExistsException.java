package com.shh.foodeliverybackendapp.exception;

/** Thrown when a unique-constraint check would fail (duplicate name, label, etc.). */
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
