package com.shh.foodeliverybackendapp.exception;

public record FieldErrorResponse(
        String field,
        String message
) {}
