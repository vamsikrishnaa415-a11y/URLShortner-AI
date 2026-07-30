package com.aiurlshortener.analytics.dto;

import java.time.Instant;

public record AnalyticsEventResponse(
        Long id,
        String shortCode,
        String originalUrl,
        Instant timestamp,
        String ipAddress,
        String browser
) {
}