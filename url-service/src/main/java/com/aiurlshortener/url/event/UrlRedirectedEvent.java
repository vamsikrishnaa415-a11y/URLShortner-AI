package com.aiurlshortener.url.event;

import java.time.Instant;

/**
 * In-process notification that a short URL was successfully resolved.
 *
 * @param shortCode resolved short code
 * @param redirectedAt redirect timestamp
 */
public record UrlRedirectedEvent(String shortCode, Instant redirectedAt) {
}