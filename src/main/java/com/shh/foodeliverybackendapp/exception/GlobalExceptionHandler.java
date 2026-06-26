package com.shh.foodeliverybackendapp.exception;

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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException ex,
                                            HttpServletRequest req) {
        log.warn(
                "API exception: status={}, message={}, path={}",
                ex.getStatus().value(),
                ex.getMessage(),
                req.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                ex.getStatus(),
                ex.getMessage());

        problemDetail.setTitle(ex.getStatus().getReasonPhrase());
        problemDetail.setProperty("path", req.getRequestURI());
        return problemDetail;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBodyValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldErrorResponse(
                        fe.getField(),
                        fe.getDefaultMessage() == null
                                ? "invalid"
                                : fe.getDefaultMessage()))
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("Request contains invalid data");
        problemDetail.setProperty("path", req.getRequestURI());
        problemDetail.setProperty("fieldErrors", fieldErrors);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest req) {

        List<FieldErrorResponse> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(v -> new FieldErrorResponse(
                       v.getPropertyPath().toString(),
                        v.getMessage()))
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("Request contains invalid parameters");
        problemDetail.setProperty("path", req.getRequestURI());
        problemDetail.setProperty("fieldErrors", fieldErrors);
        return problemDetail;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ProblemDetail handleBadRequest(Exception ex, HttpServletRequest req) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad request");
        problemDetail.setDetail("Request contains invalid request");
        problemDetail.setProperty("path", req.getRequestURI());
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex,
                                                             HttpServletRequest req) {
        // DataIntegrityViolation typically means a UNIQUE/FK constraint failed.
        // 409 Conflict is a better fit than 500.
        log.warn("Data integrity violation", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Conflict");
        problemDetail.setDetail("Data integrity violation");
        problemDetail.setProperty("path", req.getRequestURI());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAny(Exception ex, HttpServletRequest req) {
        // Catch-all: don't leak the stack trace through the API, but log it for ops.
        log.error("Unhandled exception", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(
                HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setTitle("Unhandled exception");
        problemDetail.setDetail("Unexpected server error");
        problemDetail.setProperty("path", req.getRequestURI());
        return problemDetail;
    }
}
