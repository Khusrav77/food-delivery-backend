package com.shh.foodeliverybackendapp.exception;


import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiException {

    public EntityNotFoundException(String entityType, Object id) {
        super(HttpStatus.NOT_FOUND, "%s with id %s not found".formatted(entityType, id));
    }
}
