package com.aiurlshortener.analytics.dto;

import java.time.Instant;

public record AnalyticsSummaryResponse(
        String shortCode,
        long totalClicks,
        Instant latestRedirectAt
) {
}