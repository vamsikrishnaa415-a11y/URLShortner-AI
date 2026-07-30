package com.aiurlshortener.analytics.exception;

import com.aiurlshortener.analytics.dto.AnalyticsErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
/**
 * Maps analytics validation failures to a consistent error response.
 */
public class AnalyticsExceptionHandler {

        /**
         * Handles request-body validation failures.
         *
         * @param exception validation exception
         * @param request current HTTP request
         * @return a structured bad-request response
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<AnalyticsErrorResponse> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request
        ) {
                Map<String, String> validationErrors = new LinkedHashMap<>();
                for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
                        validationErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return buildBadRequest(request, validationErrors);
        }

        /**
         * Handles controller parameter validation failures.
         *
         * @param exception validation exception
         * @param request current HTTP request
         * @return a structured bad-request response
         */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AnalyticsErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                validationErrors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return buildBadRequest(request, validationErrors);
    }

    private ResponseEntity<AnalyticsErrorResponse> buildBadRequest(
            HttpServletRequest request,
            Map<String, String> validationErrors
    ) {
        return ResponseEntity.badRequest().body(new AnalyticsErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request validation failed",
                request.getRequestURI(),
                validationErrors
        ));
    }
}