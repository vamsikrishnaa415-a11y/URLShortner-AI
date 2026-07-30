package com.aiurlshortener.analytics.service;

import com.aiurlshortener.analytics.dto.AnalyticsEventRequest;
import com.aiurlshortener.analytics.dto.AnalyticsEventResponse;
import com.aiurlshortener.analytics.dto.AnalyticsSummaryResponse;
import com.aiurlshortener.analytics.entity.AnalyticsEventEntity;
import com.aiurlshortener.analytics.repository.AnalyticsEventRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

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
        List<AnalyticsEventEntity> events = analyticsEventRepository.findByShortCodeOrderByTimestampDesc(shortCode);
        return new AnalyticsSummaryResponse(
                shortCode,
                events.size(),
                events.isEmpty() ? null : events.get(0).getTimestamp()
        );
    }
}