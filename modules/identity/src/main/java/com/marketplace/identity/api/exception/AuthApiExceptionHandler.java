package com.marketplace.identity.api.exception;

import com.marketplace.identity.application.exception.AuthenticationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public final class AuthApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthApiExceptionHandler.class);

    @ExceptionHandler(AuthenticationFailedException.class)
    public ProblemDetail handleAuthenticationFailed(
            AuthenticationFailedException exception,
            HttpServletRequest request) {
        return ProblemDetailResponses.forRequest(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                "Authentication failed",
                request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBodyValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        List<ApiFieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiFieldError(
                        error.getField(),
                        error.getDefaultMessage() == null
                                ? "invalid"
                                : error.getDefaultMessage()))
                .sorted(Comparator.comparing(ApiFieldError::field))
                .toList();

        ProblemDetail problem = ProblemDetailResponses.forRequest(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "Request contains invalid data",
                request);
        problem.setProperty("fieldErrors", fieldErrors);
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        List<ApiFieldError> fieldErrors = exception.getConstraintViolations()
                .stream()
                .map(violation -> new ApiFieldError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .sorted(Comparator.comparing(ApiFieldError::field))
                .toList();

        ProblemDetail problem = ProblemDetailResponses.forRequest(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "Request contains invalid parameters",
                request);
        problem.setProperty("fieldErrors", fieldErrors);
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableRequest(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return ProblemDetailResponses.forRequest(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                "Request body is missing or malformed",
                request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleInvalidArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {
        return ProblemDetailResponses.forRequest(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                "Request contains invalid data",
                request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataConflict(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        log.warn("Identity request conflicted with persisted data at {}",
                request.getRequestURI());
        return ProblemDetailResponses.forRequest(
                HttpStatus.CONFLICT,
                "Conflict",
                "Request conflicts with existing data",
                request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(
            Exception exception,
            HttpServletRequest request) {
        log.error("Unhandled identity API exception at {}",
                request.getRequestURI(), exception);
        return ProblemDetailResponses.forRequest(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Unexpected server error",
                request);
    }
}
