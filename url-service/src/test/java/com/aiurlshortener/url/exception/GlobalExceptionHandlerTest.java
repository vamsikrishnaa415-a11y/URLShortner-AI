package com.aiurlshortener.url.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.aiurlshortener.url.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void returnsNotFoundErrorResponse() {
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleShortUrlNotFound(
                new ShortUrlNotFoundException("A1b2C3d4"), request("/A1b2C3d4")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().status()).isEqualTo(404);
    }

    @Test
    void returnsBadRequestErrorResponseForValidationFailures() {
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolation(
                new ConstraintViolationException("Invalid short code", Set.of()), request("/invalid")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().status()).isEqualTo(400);
    }

    @Test
    void returnsConflictErrorResponse() {
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateUrl(
                new DuplicateUrlException("https://example.com/page"), request("/url")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().status()).isEqualTo(409);
    }

    @Test
    void returnsInternalServerErrorWithoutLeakingDetails() {
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnexpectedException(
                new RuntimeException("database password"), request("/url")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred");
    }

    private MockHttpServletRequest request(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        return request;
    }
}