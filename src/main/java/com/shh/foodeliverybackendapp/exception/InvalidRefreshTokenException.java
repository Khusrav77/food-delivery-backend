package com.shh.foodeliverybackendapp.exception;

public class InvalidRefreshTokenException extends  RuntimeException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
