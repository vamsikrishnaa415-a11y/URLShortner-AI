package com.aiurlshortener.url.dto;

import java.time.Instant;

public record CreateShortUrlResponse(
        String shortCode,
        String originalUrl,
        Instant createdAt,
        boolean created
) {
}