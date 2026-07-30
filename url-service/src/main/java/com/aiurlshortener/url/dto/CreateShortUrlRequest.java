package com.aiurlshortener.url.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateShortUrlRequest(
        @NotBlank(message = "originalUrl is required")
        @Size(max = 2048, message = "originalUrl must not exceed 2048 characters")
        @Pattern(regexp = "^https?://[^\\s]+$", message = "originalUrl must use HTTP or HTTPS")
        String originalUrl
) {
}