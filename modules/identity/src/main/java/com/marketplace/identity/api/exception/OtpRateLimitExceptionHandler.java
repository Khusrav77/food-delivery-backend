package com.marketplace.identity.api.exception;

import com.marketplace.identity.application.exception.OtpRequestRateLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public final class OtpRateLimitExceptionHandler {

    @ExceptionHandler(OtpRequestRateLimitExceededException.class)
    public ResponseEntity<ProblemDetail> handleRateLimit(
            OtpRequestRateLimitExceededException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.TOO_MANY_REQUESTS,
                "OTP request rate limit exceeded");
        problem.setTitle("Too Many Requests");
        problem.setProperty("retryAfterSeconds", exception.retryAfterSeconds());

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header(HttpHeaders.RETRY_AFTER, Long.toString(exception.retryAfterSeconds()))
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
