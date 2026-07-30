package com.aiurlshortener.url.event;

import java.time.Instant;

public record UrlRedirectedEvent(String shortCode, Instant redirectedAt) {
}