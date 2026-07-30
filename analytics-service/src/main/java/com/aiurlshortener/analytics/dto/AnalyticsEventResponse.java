package com.aiurlshortener.analytics.dto;

import java.time.Instant;

/**
 * Stored analytics event representation.
 *
 * @param id persistent event identifier
 * @param shortCode resolved short code
 * @param originalUrl redirect destination
 * @param timestamp redirect timestamp
 * @param ipAddress client IP address
 * @param browser client user agent
 */
public record AnalyticsEventResponse(
        Long id,
        String shortCode,
        String originalUrl,
        Instant timestamp,
        String ipAddress,
        String browser
) {
}