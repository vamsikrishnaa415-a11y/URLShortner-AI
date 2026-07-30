package com.aiurlshortener.analytics.dto;

import java.time.Instant;
import java.util.Map;

/**
 * Consistent validation error response returned by analytics endpoints.
 *
 * @param timestamp response creation time
 * @param status HTTP status code
 * @param error HTTP status reason
 * @param message client-facing error message
 * @param path request path
 * @param validationErrors field-level validation errors
 */
public record AnalyticsErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}