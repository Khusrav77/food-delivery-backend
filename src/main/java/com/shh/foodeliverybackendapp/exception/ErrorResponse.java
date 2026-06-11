package com.shh.foodeliverybackendapp.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Single shape for every error returned by the API.
 *
 * The fields are deliberately flat so the client can rely on them no matter
 * which kind of error happened. Field-level errors live in
 * {@link #fieldErrors()} and are populated only when applicable
 * (validation failures); for other errors that map is {@code null}.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<Map<String, String>> fieldErrors
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path, null);
    }

    public static ErrorResponse ofValidation(int status,
                                             String error,
                                             String message,
                                             String path,
                                             List<Map<String, String>> fieldErrors) {
        return new ErrorResponse(Instant.now(), status, error, message, path, fieldErrors);
    }
}
