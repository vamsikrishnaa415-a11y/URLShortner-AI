package com.aiurlshortener.url.dto;

import java.time.Instant;
import java.util.Map;

/**
 * Consistent error representation returned by URL service endpoints.
 *
 * @param timestamp response creation time
 * @param status HTTP status code
 * @param error HTTP status reason
 * @param message safe client-facing error message
 * @param path request path
 * @param validationErrors field-level validation errors
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}