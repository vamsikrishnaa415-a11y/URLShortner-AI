package com.aiurlshortener.url.dto;

import java.time.Instant;

/**
 * Response returned after a URL creation attempt.
 *
 * @param shortCode generated short code
 * @param originalUrl destination URL
 * @param createdAt mapping creation time
 * @param created whether a new mapping was created
 */
public record CreateShortUrlResponse(
        String shortCode,
        String originalUrl,
        Instant createdAt,
        boolean created
) {
}