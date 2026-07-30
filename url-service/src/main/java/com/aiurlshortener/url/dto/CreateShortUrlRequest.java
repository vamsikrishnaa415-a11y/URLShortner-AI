package com.aiurlshortener.url.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Validated request to create a short URL.
 *
 * @param originalUrl absolute HTTP or HTTPS destination URL
 */
public record CreateShortUrlRequest(
        @NotBlank(message = "originalUrl is required")
        @Size(max = 2048, message = "originalUrl must not exceed 2048 characters")
        @Pattern(regexp = "^https?://[^\\s]+$", message = "originalUrl must use HTTP or HTTPS")
        String originalUrl
) {
}