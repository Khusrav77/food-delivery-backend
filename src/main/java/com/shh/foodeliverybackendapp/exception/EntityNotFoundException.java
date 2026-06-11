package com.shh.foodeliverybackendapp.exception;

/**
 * Thrown when an entity is looked up by id and not found.
 *
 * Carries the type name and the missing id so the global advice can build
 * a useful error response without each service having to format its own
 * message.
 */
public class EntityNotFoundException extends RuntimeException {

    private final String entityType;
    private final Object id;

    public EntityNotFoundException(String entityType, Object id) {
        super(entityType + " with id " + id + " not found");
        this.entityType = entityType;
        this.id = id;
    }

    public String getEntityType() { return entityType; }
    public Object getId() { return id; }
}
