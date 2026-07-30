package com.aiurlshortener.analytics.service;

import com.aiurlshortener.analytics.dto.AnalyticsEventRequest;
import com.aiurlshortener.analytics.dto.AnalyticsEventResponse;
import com.aiurlshortener.analytics.dto.AnalyticsSummaryResponse;

public interface AnalyticsService {

    AnalyticsEventResponse recordEvent(AnalyticsEventRequest request);

    AnalyticsSummaryResponse getAnalytics(String shortCode);
}