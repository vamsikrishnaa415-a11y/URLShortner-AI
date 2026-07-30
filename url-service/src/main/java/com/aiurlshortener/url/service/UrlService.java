package com.aiurlshortener.url.service;

import com.aiurlshortener.url.dto.CreateShortUrlRequest;
import com.aiurlshortener.url.dto.CreateShortUrlResponse;

/**
 * Defines URL creation and redirect-resolution operations.
 */
public interface UrlService {

    /**
     * Creates a short URL for a validated destination.
     *
     * @param request URL creation request
     * @return the persisted short URL representation
     */
    CreateShortUrlResponse createShortUrl(CreateShortUrlRequest request);

    /**
     * Resolves a short code and records a successful redirect.
     *
     * @param shortCode short code to resolve
     * @return the destination URL
     */
    String resolveOriginalUrl(String shortCode);
}