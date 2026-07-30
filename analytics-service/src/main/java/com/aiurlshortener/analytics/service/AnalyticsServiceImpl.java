package com.aiurlshortener.analytics.service;

import com.aiurlshortener.analytics.dto.AnalyticsEventRequest;
import com.aiurlshortener.analytics.dto.AnalyticsEventResponse;
import com.aiurlshortener.analytics.dto.AnalyticsSummaryResponse;
import com.aiurlshortener.analytics.entity.AnalyticsEventEntity;
import com.aiurlshortener.analytics.repository.AnalyticsEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/**
 * Default implementation for storing and summarizing analytics events.
 */
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsServiceImpl(AnalyticsEventRepository analyticsEventRepository) {
        this.analyticsEventRepository = analyticsEventRepository;
    }

    @Override
    @Transactional
    public AnalyticsEventResponse recordEvent(AnalyticsEventRequest request) {
        AnalyticsEventEntity savedEvent = analyticsEventRepository.save(new AnalyticsEventEntity(
                request.shortCode(),
                request.originalUrl(),
                request.timestamp(),
                request.ipAddress(),
                request.browser()
        ));

        LOGGER.info("Recorded analytics event for shortCode={}", savedEvent.getShortCode());

        return new AnalyticsEventResponse(
                savedEvent.getId(),
                savedEvent.getShortCode(),
                savedEvent.getOriginalUrl(),
                savedEvent.getTimestamp(),
                savedEvent.getIpAddress(),
                savedEvent.getBrowser()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsSummaryResponse getAnalytics(String shortCode) {
        long totalClicks = analyticsEventRepository.countByShortCode(shortCode);
        var latestEvent = analyticsEventRepository.findFirstByShortCodeOrderByTimestampDesc(shortCode);
        LOGGER.info("Retrieved analytics summary for shortCode={} totalClicks={}", shortCode, totalClicks);
        return new AnalyticsSummaryResponse(
                shortCode,
            totalClicks,
            latestEvent.map(AnalyticsEventEntity::getTimestamp).orElse(null)
        );
    }
}