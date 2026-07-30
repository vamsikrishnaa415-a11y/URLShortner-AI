package com.aiurlshortener.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * Validated request to store a redirect analytics event.
 *
 * @param shortCode resolved short code
 * @param originalUrl redirect destination
 * @param timestamp redirect timestamp
 * @param ipAddress client IP address
 * @param browser client user agent
 */
public record AnalyticsEventRequest(
        @NotBlank(message = "shortCode is required")
        @Pattern(regexp = "^[0-9A-Za-z]{8}$", message = "shortCode must be an 8-character Base62 value")
        String shortCode,
        @NotBlank(message = "originalUrl is required")
        @Size(max = 2048, message = "originalUrl must not exceed 2048 characters")
        @Pattern(regexp = "^https?://[^\\s]+$", message = "originalUrl must use HTTP or HTTPS")
        String originalUrl,
        @NotNull(message = "timestamp is required")
        Instant timestamp,
        @NotBlank(message = "ipAddress is required")
        @Size(max = 45, message = "ipAddress must not exceed 45 characters")
        String ipAddress,
        @NotBlank(message = "browser is required")
        @Size(max = 512, message = "browser must not exceed 512 characters")
        String browser
) {
}