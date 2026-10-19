package com.shh.foodeliverybackendapp.exception;


import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends ApiException {

    public EntityAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
