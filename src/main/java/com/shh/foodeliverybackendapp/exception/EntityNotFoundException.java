package com.shh.foodeliverybackendapp.exception;


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
