package com.aiurlshortener.analytics.service;

import com.aiurlshortener.analytics.dto.AnalyticsEventRequest;
import com.aiurlshortener.analytics.dto.AnalyticsEventResponse;
import com.aiurlshortener.analytics.dto.AnalyticsSummaryResponse;

/**
 * Defines analytics event recording and summary retrieval operations.
 */
public interface AnalyticsService {

    /**
     * Stores one redirect analytics event.
     *
     * @param request validated event request
     * @return stored event representation
     */
    AnalyticsEventResponse recordEvent(AnalyticsEventRequest request);

    /**
     * Retrieves aggregate analytics for a short code.
     *
     * @param shortCode short code to aggregate
     * @return click count and latest redirect time
     */
    AnalyticsSummaryResponse getAnalytics(String shortCode);
}