package com.aiurlshortener.analytics.dto;

import java.time.Instant;
import java.util.Map;

public record AnalyticsErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}