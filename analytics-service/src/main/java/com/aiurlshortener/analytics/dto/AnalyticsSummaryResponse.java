package com.aiurlshortener.analytics.dto;

import java.time.Instant;

/**
 * Aggregate analytics response for a short code.
 *
 * @param shortCode requested short code
 * @param totalClicks total recorded redirects
 * @param latestRedirectAt latest redirect timestamp, when available
 */
public record AnalyticsSummaryResponse(
        String shortCode,
        long totalClicks,
        Instant latestRedirectAt
) {
}